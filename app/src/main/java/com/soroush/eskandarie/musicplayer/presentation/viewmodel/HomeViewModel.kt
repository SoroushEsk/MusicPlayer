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
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.GetPlaylistWithAllMusicFileByIdUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetMusicFileByIdFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.ModifyMusicStatusUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.GetAllPlaylistItemsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.RefreshQueueUseCase
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.nav.Destination
import com.soroush.eskandarie.musicplayer.presentation.state.HomeViewModelState
import com.soroush.eskandarie.musicplayer.presentation.state.PlaybackStates
import com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode
import com.soroush.eskandarie.musicplayer.presentation.state.SearchFieldState
import com.soroush.eskandarie.musicplayer.shared_component.paging.ListPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val getAllMusicFromDatabaseUseCase: GetAllMusicFromDatabaseUseCase,
    private val getAllPlaylistItemsUseCase: GetAllPlaylistItemsUseCase,
    private val getPlaylistWithAllMusic: GetPlaylistWithAllMusicFileByIdUseCase,
    private val refreshQueueUseCase: RefreshQueueUseCase,
    private val modifyMusicStatusUseCase: ModifyMusicStatusUseCase,
    private val getMusicFileByIdUseCase: GetMusicFileByIdFromDatabaseUseCase
): ViewModel() {
    //region Viewmodel States
    private lateinit var mediaController: MediaController
    private var isMediaControllerInitialized: Boolean = false
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
        artist =  "Unknown_Artist",
        title = "Unknown_Title",
        bitmapBitmap = uriToBitmap(applicationContext, null)
    ))
    val playbackState: StateFlow<PlaybackStates> = _playbackState.asStateFlow()

    var musicList: Flow<PagingData<MusicFile>> = flowOf(PagingData.empty())

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
                    is HomeViewModelSetStateAction.OnNextMusic                  -> onMusicChange(action.nextMusic)
                    is HomeViewModelSetStateAction.UpdateTitle                  -> setTitle(action.title)
                    is HomeViewModelSetStateAction.SetPlayState                 -> setPlayState(action.isMusicPlaying)
                    is HomeViewModelSetStateAction.UpdateArtist                 -> setArtist(action.artist)
                    is HomeViewModelSetStateAction.PausePlayback                -> pausePlayback()
                    is HomeViewModelSetStateAction.SetRepeatMode                -> setRepeatMode(action.repeatMode)
                    is HomeViewModelSetStateAction.UpdateArtWork                -> setArtWork(action.artWork)
                    is HomeViewModelSetStateAction.SetUpMusicList               -> setupMusicList(action.id, action.route)
                    is HomeViewModelSetStateAction.ResumePlayback               -> resumePlayback()
                    is HomeViewModelSetStateAction.UpdatePlayCount              -> {}
                    is HomeViewModelSetStateAction.SetShuffleState              -> setShuffleStatus(action.isShuffle)
                    is HomeViewModelSetStateAction.ForwardPlayback              -> forwardPlayback()
                    is HomeViewModelSetStateAction.SetMusicPercent              -> setSongPercent()
                    is HomeViewModelSetStateAction.GetAllPlaylists              -> getAllPlaylists()
                    is HomeViewModelSetStateAction.BackwardPlayback             -> backwardPlayback()
                    is HomeViewModelSetStateAction.GetAllMusicFiles             -> {}
                    is HomeViewModelSetStateAction.UpdateDatePlayed             -> updateDatePlayed()
                    is HomeViewModelSetStateAction.ResetLazyListState           -> resetLazyListState()
                    is HomeViewModelSetStateAction.SetStateSearchText           -> setSearchText(action.searchText)
                    is HomeViewModelSetStateAction.SetCurrentDuration           -> setCurrentDuration(action.currentDuration)
                    is HomeViewModelSetStateAction.UpdateMusicDetails           -> setMusicDetails()
                    is HomeViewModelSetStateAction.ChangeFavoriteState          -> setFavoriteState(action.isFavorite)
                    is HomeViewModelSetStateAction.SetCurrentPlaylistName       -> setNewPlaylistLazyListState(action.playlistName)
                    is HomeViewModelSetStateAction.SetMediaControllerObserver   -> setMediaController(action.mediaController)
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
            BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
        }
    }
    //endregion
    //region Change PlayBack Methods
    private fun pausePlayback(){
        mediaController.pause()
    }
    private fun resumePlayback(){
        mediaController.play()
    }
    private fun forwardPlayback(){
        mediaController.seekToNext()
    }
    private fun backwardPlayback() {
        mediaController.seekToPrevious()
    }
    //endregion
    //region Get State Function
    fun viewModelGetStateActions(action: HomeViewModelGetStateAction): StateFlow<*>{
        return when(action){
            is HomeViewModelGetStateAction.GetPlaylists         -> playlistItems
            is HomeViewModelGetStateAction.GetMusicStatus       -> playbackState
            is HomeViewModelGetStateAction.GetMusicFiles        -> playbackState
            is HomeViewModelGetStateAction.GetSearchTextState   -> homeState
            is HomeViewModelGetStateAction.GetLazyListState     -> lazyListState
        }
    }
    fun getMusicPageList(): Flow<PagingData<MusicFile>> = musicList
    //endregion
    //region Set State Functions
    private fun setupMusicList(id: Long, route: String){
        viewModelScope.launch {
            musicList = if(id == -1L){
                when(route){
                    Destination.AllMusicScreen.route -> getAllMusicFromDatabaseUseCase()
                    else -> getAllMusicFromDatabaseUseCase()
                }
            } else {
                val playlistWithMusic = getPlaylistWithAllMusic(id)
                val pager = Pager(PagingConfig(pageSize = 30)){
                    ListPagingSource<MusicFile>(playlistWithMusic.musicList.map { it.toMusicFile() })
                }
                pager.flow
            }
        }
    }
    private fun setMediaController(newMediaController: MediaController){
        mediaController = newMediaController
        isMediaControllerInitialized = true
        setMusicDetails()
    }
    private fun backToHomeScreen(){
        _playlistName.value = ""
    }
    private fun setNewPlaylistLazyListState(playlistName: String) {
        _lazyListState.value = LazyListState()
        _playlistName.value = playlistName
    }
//    private fun getAllMusicFiles(){
//        viewModelScope.launch {
//            getAllMusicFromDatabaseUseCase()
//                .cachedIn(viewModelScope)
//                .collectLatest { pagingData ->
//                    _musicList.value = pagingData
//                }
//        }
//    }
    private fun getAllPlaylists(){
        viewModelScope.launch {
            _playlistItems.value = getAllPlaylistItemsUseCase()
        }
    }
    private fun setSongPercent(){
        if( isMediaControllerInitialized.not() ) return
        val currentDuration = mediaController.currentPosition.toFloat()
        setCurrentDuration(currentDuration.toLong())
        val totalDuration = mediaController.contentDuration.toFloat()
        updateTotalDuration(totalDuration.toLong())
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
        mediaController.shuffleModeEnabled = isShuffle
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
    private fun resetLazyListState(){
        _lazyListState.value = LazyListState()
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
        if(newDuration>=0) {
            _playbackState.update {
                it.copy(
                    totalDuration = newDuration
                )
            }
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
    private fun setMusicDetails() {
        _playbackState.update {
            it.copy(
                artist = mediaController.currentMediaItem?.mediaMetadata?.artist?.toString() ?: "Unknown_Artist",
                title = mediaController.currentMediaItem?.mediaMetadata?.title?.toString() ?: "Unknown_Title",
                bitmapBitmap = uriToBitmap(applicationContext, mediaController.currentMediaItem?.mediaMetadata?.artworkUri)
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