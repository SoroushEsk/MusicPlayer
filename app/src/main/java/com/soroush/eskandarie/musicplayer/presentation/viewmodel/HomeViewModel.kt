package com.soroush.eskandarie.musicplayer.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import androidx.media3.session.legacy.MediaControllerCompat
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetMusicFileByIdFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.ModifyMusicStatusUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.GetAllPlaylistItemsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.RefreshQueueUseCase
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.state.HomeViewModelState
import com.soroush.eskandarie.musicplayer.presentation.state.PlaybackStates
import com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode
import com.soroush.eskandarie.musicplayer.presentation.state.SearchFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val mediaSession: MediaSession,
    private val getAllMusicFromDatabaseUseCase: GetAllMusicFromDatabaseUseCase,
    private val getAllPlaylistItemsUseCase: GetAllPlaylistItemsUseCase,
    private val refreshQueueUseCase: RefreshQueueUseCase,
    private val modifyMusicStatusUseCase: ModifyMusicStatusUseCase,
    private val getMusicFileByIdUseCase: GetMusicFileByIdFromDatabaseUseCase
): ViewModel() {
    //region Viewmodel States
    private val _homeState = MutableStateFlow(
        HomeViewModelState(
            SearchFieldState(
                searchText = "",
                isSearch = false
            )
        )
    )
     val homeState : StateFlow<HomeViewModelState>
        get() = _homeState

    private val _playbackState: MutableStateFlow<PlaybackStates> = MutableStateFlow(PlaybackStates(
        artist = mediaSession.player.currentMediaItem?.mediaMetadata?.artist?.toString() ?: "Unknown_Artist",
        title = mediaSession.player.currentMediaItem?.mediaMetadata?.title?.toString() ?: "Unknown_Title",
        bitmapBitmap = uriToBitmap(applicationContext, mediaSession.player.currentMediaItem?.mediaMetadata?.artworkUri)
    ))
    val playbackState: StateFlow<PlaybackStates> = _playbackState.asStateFlow()

    private val _musicList: MutableStateFlow<List<MusicFile>> = MutableStateFlow(emptyList())
    val musicList: StateFlow<List<MusicFile>> = _musicList.asStateFlow()

    private val _playlistItems: MutableStateFlow<List<Playlist>> = MutableStateFlow(emptyList())
    val playlistItems: StateFlow<List<Playlist>>
        get() = _playlistItems.asStateFlow()

    private val _lazyListState = MutableStateFlow(LazyListState())
    val lazyListState: StateFlow<LazyListState> = _lazyListState.asStateFlow()

    private val _playlistName = MutableStateFlow("")
    val playlistName: StateFlow<String> = _playlistName.asStateFlow()

    //endregion
    //region Viewmodel Action Channels
    private val setActionChannel = Channel<HomeViewModelSetStateAction> ( Channel.UNLIMITED )
    //endregion
    //region Main Methods
    init{
        handleSetActions()
    }
    fun viewModelSetAction(action: HomeViewModelSetStateAction){
        viewModelScope.launch {
            setActionChannel.send(action)
        }
    }
    private fun handleSetActions(){
        viewModelScope.launch {
            setActionChannel.receiveAsFlow().collect{ action ->
                when( action ){
                    is HomeViewModelSetStateAction.SetStateSearchText       -> setSearchText(action.searchText)
                    is HomeViewModelSetStateAction.GetAllMusicFiles         -> getAllMusicFiles()
                    is HomeViewModelSetStateAction.GetAllPlaylists          -> getAllPlaylists()
                    is HomeViewModelSetStateAction.SetMusicPercent          -> setSongPercent()
                    is HomeViewModelSetStateAction.SetPlayState             -> setPlayState(action.isMusicPlaying)
                    is HomeViewModelSetStateAction.SetShuffleState          -> setShuffleStatus(action.isShuffle)
                    is HomeViewModelSetStateAction.SetRepeatMode            -> setRepeatMode(action.repeatMode)
                    is HomeViewModelSetStateAction.SetCurrentDuration       -> setCurrentDuration(action.currentDuration)
                    is HomeViewModelSetStateAction.OnNextMusic              -> onMusicChange(action.nextMusic)
                    is HomeViewModelSetStateAction.ChangeFavoriteState      -> setFavoriteState(action.isFavorite)
                    is HomeViewModelSetStateAction.UpdateDatePlayed         -> updateDatePlayed()
                    is HomeViewModelSetStateAction.SetCurrentPlaylistName   -> setNewPlaylistLazyListState(action.playlistName)
                    is HomeViewModelSetStateAction.UpdatePlayCount          -> {}
                    is HomeViewModelSetStateAction.UpdateMusicDetails       -> setMusicDetails()
                    is HomeViewModelSetStateAction.UpdateTitle              -> setTitle(action.title)
                    is HomeViewModelSetStateAction.UpdateArtist             -> setArtist(action.artist)
                    is HomeViewModelSetStateAction.UpdateArtWork            -> setArtWork(action.artWork)
                }
            }
        }
    }
    private fun uriToBitmap(context: Context, uri: Uri?): Bitmap {
        return try {
            if (uri == null) {
                BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
            } else {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                } ?: BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
            }
        } catch (e: Exception) {
            Log.e("UriToBitmap", "Error converting URI to bitmap: ${e.message}")
            BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
        }
    }
    //endregion
    //region Get State Function
    fun viewModelGetStateActions(action: HomeViewModelGetStateAction): StateFlow<*>{
        return when(action){
            is HomeViewModelGetStateAction.GetPlaylists         -> playlistItems
            is HomeViewModelGetStateAction.GetMusicStatus       -> playbackState
            is HomeViewModelGetStateAction.GetMusicFiles        -> musicList
            is HomeViewModelGetStateAction.GetSearchTextState   -> homeState
            is HomeViewModelGetStateAction.GetLazyListState     -> lazyListState
        }
    }
    //endregion
    //region Make A Change Functions
    private fun backToHomeScreen(){
        _playlistName.value = ""
    }
    private fun setNewPlaylistLazyListState(playlistName: String) {
        _lazyListState.value = LazyListState()
        _playlistName.value = playlistName
    }
    private fun getAllMusicFiles(){
        viewModelScope.launch {
            _musicList.value = getAllMusicFromDatabaseUseCase()

        }
    }
    private fun getAllPlaylists(){
        viewModelScope.launch {
            _playlistItems.value = getAllPlaylistItemsUseCase()
        }
    }
    private fun setSongPercent(){
        val currentDuration = mediaSession.player.currentPosition.toFloat()
        setCurrentDuration(currentDuration.toLong())
        val totalDuration = mediaSession.player.contentDuration
        updateTotalDuration(totalDuration)
        updateSongPercent(currentDuration / totalDuration)
    }
    private fun setPlayState(isMusicPlaying: Boolean){
        _playbackState.update {
            it.copy(
                isPlaying = isMusicPlaying
            )
        }
    }
    private fun setShuffleStatus(isShuffle: Boolean){
        _playbackState.update {
            it.copy(
                isShuffle = isShuffle
            )
        }
    }
    private fun setRepeatMode(repeatMode: RepeatMode){
        _playbackState.update {
            it.copy(
                repeatMode = repeatMode
            )
        }
    }
    private fun setCurrentDuration(currentDuration: Long){
        _playbackState.update {
            it.copy(
                currentDuration = currentDuration
            )
        }
    }
    private fun onMusicChange(nextMusicFileId: Long){
//        viewModelScope.launch {
//            modifyMusicStatusUseCase(_playbackState.value.currentMusicFile)
//        }
//        viewModelScope.launch {
//            val nextMusicFile = getMusicFileByIdUseCase(nextMusicFileId)
//            if (nextMusicFile != null) {
//                _playbackState.update {
//                    it.copy(
//                    )
//                }
//            }
//        }
    }
    private fun updatePlayCount(){
//        Todo("playCoutn")
//        _playbackState.update {
//            it.copy(
//                currentMusicFile = it.currentMusicFile.copy(
//                    playCount = it.currentMusicFile.playCount + 1
//                )
//            )
//        }
    }
    private fun updateDatePlayed(){
//        _playbackState.update {
//            it.copy(
//                currentMusicFile = it.currentMusicFile.copy(
//                    datePlayed = System.currentTimeMillis()
//                )
//            )
//        }Todo("dataplayed")
    }
    private fun setFavoriteState(isFavorite: Boolean){
        _playbackState.update {
            it.copy(
                isFavorite = isFavorite
            )
        }
    }
    private fun updateSongPercent(newPercent: Float) {
        _playbackState.update {
            it.copy(
                musicPercent = newPercent
            )
        }
    }
    private fun updateTotalDuration(newDuration: Long){
        _playbackState.update {
            it.copy(
                totalDuration = newDuration
            )
        }
    }
    private fun setSearchText(searchText: String){
        _homeState.update {
            it.copy(
                searchFieldState =  it.searchFieldState.copy(
                    searchText = searchText
                )
            )
        }
    }
    private fun setArtist(artist: String) {
        _playbackState.update {
            it.copy(
                artist = artist
            )
        }
    }
    private fun setTitle(title: String){
        _playbackState.update {
            it.copy(
                title = title
            )
        }
    }
    private fun setArtWork(artwork: Bitmap){
        _playbackState.update {
            it.copy(
                bitmapBitmap = artwork
            )
        }
    }
    private fun setMusicDetails(){
        _playbackState.update {
            it.copy(
                artist = mediaSession.player.currentMediaItem?.mediaMetadata?.artist.toString(),
                title = mediaSession.player.currentMediaItem?.mediaMetadata?.title.toString(),
                bitmapBitmap = uriToBitmap(applicationContext, mediaSession.player.currentMediaItem?.mediaMetadata?.artworkUri)
            )
        }
    }

    //endregion
    //region Override Methods
    override fun onCleared() {
        super.onCleared()
        setActionChannel.close()
    }
    //endregion
}