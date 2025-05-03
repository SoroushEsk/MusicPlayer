package com.soroush.eskandarie.musicplayer.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity
import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistMusicRelationEntity
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.model.getAlbumArtBitmap
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDatabasePagerUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.AnalyzeFoldersUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.Get100MostPlayedMusicsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.Get100RecentlyPlayedMusicListUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetFavoriteMusicFilesPagerUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetListOfAllMusicUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetListOfFavoriteMusicUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music.GetPlaylistWithAllMusicFileByIdUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetMusicFileByIdFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetTracksWithUsualOrderLimitedUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.ModifyMusicStatusUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.UpdateFavoriteMusicByIdUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.CreateANewPlaylistUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.GetAllPlaylistItemsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.GetNumberOfPlaylistsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music.AddAMusicToPlaylistUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music.AddListOfMusicToAPlaylistUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.RefreshQueueUseCase
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.nav.Destination
import com.soroush.eskandarie.musicplayer.presentation.state.FourTopPlaylistImageState
import com.soroush.eskandarie.musicplayer.presentation.state.HomeViewModelState
import com.soroush.eskandarie.musicplayer.presentation.state.PlaybackStates
import com.soroush.eskandarie.musicplayer.presentation.state.PlaylistType
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val getAllMusicFromDatabasePagerUseCase: GetAllMusicFromDatabasePagerUseCase,
    private val getAllPlaylistItemsUseCase: GetAllPlaylistItemsUseCase,
    private val getPlaylistWithAllMusic: GetPlaylistWithAllMusicFileByIdUseCase,
    private val numberrOfPlaylists: GetNumberOfPlaylistsUseCase,
    private val createANewPlaylist: CreateANewPlaylistUseCase,
    private val getFavoriteMusicFiles: GetFavoriteMusicFilesPagerUseCase,
    private val analyzeFoldersUseCase: AnalyzeFoldersUseCase,
    private val addMusicListToPlaylist: AddListOfMusicToAPlaylistUseCase,
    private val get100MostPlayed: Get100MostPlayedMusicsUseCase,
    private val get100RecentlyPlayed: Get100RecentlyPlayedMusicListUseCase,
    private val getMusicIdOrdered: GetTracksWithUsualOrderLimitedUseCase,
    private val addAMusicToPlaylist: AddAMusicToPlaylistUseCase,
    private val refreshQueueUseCase: RefreshQueueUseCase,
    private val modifyMusicStatusUseCase: ModifyMusicStatusUseCase,
    private val getMusicFileByIdUseCase: GetMusicFileByIdFromDatabaseUseCase,
    private val updateMusicFavoriteState: UpdateFavoriteMusicByIdUseCase,
    private val getListOfFavoriteMusic: GetListOfFavoriteMusicUseCase,
    private val getListAllMusic: GetListOfAllMusicUseCase
) : ViewModel() {
    //region Viewmodel States


    private val actionHandlers: Map<KClass<out HomeViewModelSetStateAction>, (HomeViewModelSetStateAction) -> Unit> =
        mapOf(
            HomeViewModelSetStateAction.SetCurrentPlaylist::class to { action -> setPlaylistType((action as HomeViewModelSetStateAction.SetCurrentPlaylist).playlist) },
            HomeViewModelSetStateAction.OnNextMusic::class to { _ -> onMusicChange() },
            HomeViewModelSetStateAction.UpdateTitle::class to { action -> setTitle((action as HomeViewModelSetStateAction.UpdateTitle).title) },
            HomeViewModelSetStateAction.SetPlayState::class to { action -> setPlayState((action as HomeViewModelSetStateAction.SetPlayState).isMusicPlaying) },
            HomeViewModelSetStateAction.UpdateArtist::class to { action -> setArtist((action as HomeViewModelSetStateAction.UpdateArtist).artist) },
            HomeViewModelSetStateAction.PausePlayback::class to { _ -> pausePlayback() },
            HomeViewModelSetStateAction.SetRepeatMode::class to { action -> setRepeatMode((action as HomeViewModelSetStateAction.SetRepeatMode).repeatMode) },
            HomeViewModelSetStateAction.UpdateArtWork::class to { action -> setArtWork((action as HomeViewModelSetStateAction.UpdateArtWork).artWork) },
            HomeViewModelSetStateAction.SetUpMusicList::class to { action ->
                setupMusicList(
                    (action as HomeViewModelSetStateAction.SetUpMusicList).id,
                    action.route,
                    action.folderName
                )
            },
            HomeViewModelSetStateAction.ResumePlayback::class to { _ -> resumePlayback() },
            HomeViewModelSetStateAction.SetShuffleState::class to { action -> setShuffleStatus((action as HomeViewModelSetStateAction.SetShuffleState).isShuffle) },
            HomeViewModelSetStateAction.ForwardPlayback::class to { _ -> forwardPlayback() },
            HomeViewModelSetStateAction.SetMusicPercent::class to { _ -> setSongPercent() },
            HomeViewModelSetStateAction.GetAllPlaylists::class to { _ -> getAllPlaylists() },
            HomeViewModelSetStateAction.AddANewPlaylist::class to { action -> addNewPlaylist((action as HomeViewModelSetStateAction.AddANewPlaylist).name) },
            HomeViewModelSetStateAction.BackwardPlayback::class to { _ -> backwardPlayback() },
            HomeViewModelSetStateAction.FillFolderRequirements::class to { _ -> setFolderMusicMap() },
            HomeViewModelSetStateAction.AddMusicToPlaylist::class to { action ->
                addSongToAPlaylist(
                    (action as HomeViewModelSetStateAction.AddMusicToPlaylist).musicId
                )
            },
            HomeViewModelSetStateAction.ResetLazyListState::class to { _ -> resetLazyListState() },
            HomeViewModelSetStateAction.SetStateSearchText::class to { action -> setSearchText((action as HomeViewModelSetStateAction.SetStateSearchText).searchText) },
            HomeViewModelSetStateAction.SetCurrentDuration::class to { action ->
                setCurrentDuration(
                    (action as HomeViewModelSetStateAction.SetCurrentDuration).currentDuration
                )
            },
            HomeViewModelSetStateAction.UpdateMusicDetails::class to { _ -> setMusicDetails() },
            HomeViewModelSetStateAction.ChangeFavoriteState::class to { action -> setFavoriteState((action as HomeViewModelSetStateAction.ChangeFavoriteState).isFavorite) },
            HomeViewModelSetStateAction.UpdateTopPlaylistState::class to { _ -> setTopPlaylistState() },
            HomeViewModelSetStateAction.SetMediaControllerObserver::class to { action ->
                setMediaController(
                    (action as HomeViewModelSetStateAction.SetMediaControllerObserver).mediaController
                )
            },
            HomeViewModelSetStateAction.PutPlaylistToQueue::class to { action ->
                putPlaylistInPlayQueue(
                    (action as HomeViewModelSetStateAction.PutPlaylistToQueue).playlistType
                )
            }
        )

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
    val homeState: StateFlow<HomeViewModelState>
        get() = _homeState

    private val _isTopPlaylistState = MutableStateFlow(false)
    private val _topPlaylistState = MutableStateFlow(FourTopPlaylistImageState())
    val topPlaylistState: StateFlow<FourTopPlaylistImageState> = _topPlaylistState.asStateFlow()

    private val _currentPlaylist = MutableStateFlow<PlaylistType>(PlaylistType.TopPlaylist("", ""))
    val currentPlaylist: StateFlow<PlaylistType> = _currentPlaylist.asStateFlow()

    private val _playbackState: MutableStateFlow<PlaybackStates> = MutableStateFlow(
        PlaybackStates(
            artist = "Unknown_Artist",
            title = "Unknown_Title",
            bitmapBitmap = uriToBitmap(applicationContext, null)
        )
    )
    val playbackState: StateFlow<PlaybackStates> = _playbackState.asStateFlow()

    var musicList: Flow<PagingData<MusicFile>> = flowOf(PagingData.empty())

    private val _folder_music_map: MutableStateFlow<Map<String, List<MusicFile>>> =
        MutableStateFlow(emptyMap())
    val folder_music_map: StateFlow<Map<String, List<MusicFile>>> = _folder_music_map.asStateFlow()

    private val _playlistItems: MutableStateFlow<List<Playlist>> = MutableStateFlow(
        emptyList()
    )
    val playlistItems: StateFlow<List<Playlist>>
        get() = _playlistItems.asStateFlow()

    private val _lazyListState = MutableStateFlow(LazyListState())
    val lazyListState: StateFlow<LazyListState> = _lazyListState.asStateFlow()

    //endregion
    //region Viewmodel Action Channels
    private val setActionChannel = Channel<HomeViewModelSetStateAction>(Channel.UNLIMITED)

    //endregion
    //region Main Methods
    init {
        handleSetActions()
    }

    fun viewModelSetAction(action: HomeViewModelSetStateAction) {
        viewModelScope.launch {
            setActionChannel.send(action)
        }
    }

    private fun handleSetActions() {
        viewModelScope.launch {
            setActionChannel.receiveAsFlow().collect { action ->
                actionHandlers[action::class]?.invoke(action) ?: handleUnknownAction(action)
            }
        }
    }

    private fun handleUnknownAction(action: HomeViewModelSetStateAction) {
        println("Unhandled action: $action")
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

    private fun getArtworkUri(context: Context, audioPath: String): Uri? {
        try {
            val bitmap = MusicFile.getAlbumArtBitmap(audioPath, context)
            // Check if bitmap is valid
            if (bitmap != null && !bitmap.isRecycled && bitmap.width > 0) {
                val file = File(context.cacheDir, "cover_${audioPath.hashCode()}.jpg")
                FileOutputStream(file).use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                }
                return FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            }
        } catch (e: Exception) {
            Log.e("MusicPlaybackService", "Error saving artwork: ${e.message}")
        }
        // Return a default artwork URI or null
        return null
    }

    //endregion
    //region Change PlayBack Methods
    private fun pausePlayback() {
        mediaController.pause()
    }

    private fun resumePlayback() {
        mediaController.play()
    }

    private fun forwardPlayback() {
        mediaController.seekToNext()
    }

    private fun backwardPlayback() {
        mediaController.seekToPrevious()
    }

    //endregion
    //region Get State Function
    fun viewModelGetStateActions(action: HomeViewModelGetStateAction): StateFlow<*> {
        return when (action) {
            is HomeViewModelGetStateAction.GetPlaylists -> playlistItems
            is HomeViewModelGetStateAction.GetMusicStatus -> playbackState
            is HomeViewModelGetStateAction.GetMusicFiles -> playbackState//
            is HomeViewModelGetStateAction.GetSearchTextState -> homeState
            is HomeViewModelGetStateAction.GetTopPlaylistState -> topPlaylistState
            is HomeViewModelGetStateAction.GetLazyListState -> lazyListState
            is HomeViewModelGetStateAction.GetFolderList -> folder_music_map
            is HomeViewModelGetStateAction.GetCurrentPlaylist -> currentPlaylist
        }
    }

    fun getMusicPageList(): Flow<PagingData<MusicFile>> = musicList

    //endregion
    //region Set State Functions
    private fun setPlaylistType(playlistType: PlaylistType) {
        _currentPlaylist.update {
            playlistType
        }
    }

    private fun putPlaylistInPlayQueue(playlistType: PlaylistType) {
        viewModelScope.launch {
            val musicList = when (playlistType) {
                is PlaylistType.UserPlayList ->
                    getPlaylistWithAllMusic(playlistType.id).musicList.map { it.toMusicFile() }

                is PlaylistType.FolderPlaylist ->
                    folder_music_map.value.getOrDefault(playlistType.folderName, listOf())

                is PlaylistType.TopPlaylist -> when (playlistType.route) {
                    Destination.FavoriteMusicScreen.route -> getListOfFavoriteMusic()
                    Destination.AllMusicScreen.route -> getListAllMusic()
                    Destination.MostPlayedScreen.route -> get100MostPlayed()
                    Destination.RecentlyPlayedScreen.route -> get100RecentlyPlayed()
                    else -> emptyList()
                }
            }
            mediaController.setMediaItems(musicList.map {
                val mediaItem = MediaItem.Builder()
                    .setMediaId(it.id.toString())
                    .setUri(it.path)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(it.title)
                            .setArtist(it.artist)
                            .setDescription(it.id.toString())
                            .setArtworkUri(getArtworkUri(applicationContext, it.path))
                            .build()
                    )
                    .build()
                mediaItem
            }
            )
            refreshQueueUseCase(
                musicList.map {
                    MusicQueueEntity(
                        id = it.id,
                        path = it.path,
                        isFavorite = it.isFavorite
                    )
                }
            )
        }
    }

    private fun setFolderMusicMap() {
        viewModelScope.launch {
            _folder_music_map.value = analyzeFoldersUseCase()
        }
    }

    private fun addNewPlaylist(playlistName: String) {
        viewModelScope.launch {
            val playlist =
                Playlist(
                    id = numberrOfPlaylists().toLong() + 1,
                    name = playlistName,
                    poster = ""
                )
            _playlistItems.update {
                it + playlist
            }
            createANewPlaylist(
                playlist
            )
        }
    }

    private fun setTopPlaylistState() {
        if (_isTopPlaylistState.value) return
        viewModelScope.launch {
            try {
                _topPlaylistState.update {
                    val idOrdered = getMusicIdOrdered(3)
                    val recentlyPlayed = get100RecentlyPlayed(3)
                    val mostPlayed = get100MostPlayed(3)
                    it.copy(
                        MostPlayed = it.MostPlayed.copy(
                            name = "Most Played",
                            front = mostPlayed.get(0).getAlbumArtBitmap() ?: it.MostPlayed.front,
                            back_left = mostPlayed.get(1).getAlbumArtBitmap()
                                ?: it.MostPlayed.back_left,
                            back_right = mostPlayed.get(2).getAlbumArtBitmap()
                                ?: it.MostPlayed.back_right
                        ),
                        AllMusic = it.MostPlayed.copy(
                            name = "All Tracks",
                            front = idOrdered.get(0).getAlbumArtBitmap() ?: it.MostPlayed.front,
                            back_left = idOrdered.get(1).getAlbumArtBitmap()
                                ?: it.MostPlayed.back_left,
                            back_right = idOrdered.get(2).getAlbumArtBitmap()
                                ?: it.MostPlayed.back_right
                        ),
                        RecentrlyPlayed = it.MostPlayed.copy(
                            name = "Recently Played",
                            front = recentlyPlayed.get(0).getAlbumArtBitmap()
                                ?: it.MostPlayed.front,
                            back_left = recentlyPlayed.get(1).getAlbumArtBitmap()
                                ?: it.MostPlayed.back_left,
                            back_right = recentlyPlayed.get(2).getAlbumArtBitmap()
                                ?: it.MostPlayed.back_right
                        )
                    )
                }
                _isTopPlaylistState.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupMusicList(id: Long, route: String, folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            musicList = if (id == -1L) {
                when (route) {
                    Destination.AllMusicScreen.route -> getAllMusicFromDatabasePagerUseCase()
                    Destination.MostPlayedScreen.route -> {
                        val mostPlayedMusics = get100MostPlayed()
                        val pager = Pager(PagingConfig(pageSize = 100)) {
                            ListPagingSource<MusicFile>(mostPlayedMusics)
                        }
                        pager.flow
                    }

                    Destination.RecentlyPlayedScreen.route -> {
                        val recentlyPlayed = get100RecentlyPlayed()
                        val pager = Pager(PagingConfig(pageSize = 100)) {
                            ListPagingSource<MusicFile>(recentlyPlayed)
                        }
                        pager.flow
                    }

                    Destination.FolderMusicScreen.route -> {
                        val folderMusicList =
                            folder_music_map.value.getOrDefault(folderName, listOf())
                        val pager = Pager(PagingConfig(pageSize = 50)) {
                            ListPagingSource<MusicFile>(folderMusicList)
                        }
                        pager.flow
                    }

                    Destination.FavoriteMusicScreen.route -> getFavoriteMusicFiles()
                    else -> getAllMusicFromDatabasePagerUseCase()
                }
            } else {
                val playlistWithMusic = getPlaylistWithAllMusic(id)
                val pager = Pager(PagingConfig(pageSize = 30)) {
                    ListPagingSource<MusicFile>(playlistWithMusic.musicList.map { it.toMusicFile() })
                }
                pager.flow
            }
        }
    }

    private fun setMediaController(newMediaController: MediaController) {
        mediaController = newMediaController
        isMediaControllerInitialized = true
        setMusicDetails()
    }

    private fun setNewPlaylistLazyListState() {
        _lazyListState.value = LazyListState()
    }

    private fun getAllPlaylists() {
        viewModelScope.launch {
            _playlistItems.value = getAllPlaylistItemsUseCase().toMutableList()
        }
    }

    private fun addSongToAPlaylist(musicId: Long) {
        viewModelScope.launch {
            addAMusicToPlaylist(PlaylistMusicRelationEntity(3L, musicId))
        }
    }

    private fun setSongPercent() {
        if (isMediaControllerInitialized.not()) return
        val currentDuration = mediaController.currentPosition.toFloat()
        setCurrentDuration(currentDuration.toLong())
        val totalDuration = mediaController.contentDuration.toFloat()
        updateTotalDuration(totalDuration.toLong())
        updateSongPercent(currentDuration / totalDuration)
    }

    private fun setPlayState(isMusicPlaying: Boolean) {
        _playbackState.update {
            it.copy(
                isPlaying = isMusicPlaying
            )
        }
    }

    private fun setShuffleStatus(isShuffle: Boolean) {
        mediaController.shuffleModeEnabled = isShuffle
        _playbackState.update {
            it.copy(
                isShuffle = isShuffle
            )
        }
    }

    private fun setRepeatMode(repeatMode: RepeatMode) {
        mediaController.repeatMode = when (repeatMode) {
            RepeatMode.No_Repeat -> Player.REPEAT_MODE_OFF
            RepeatMode.Repeat_All -> Player.REPEAT_MODE_ALL
            RepeatMode.Repeat_Once -> Player.REPEAT_MODE_ONE
        }
        _playbackState.update {
            it.copy(
                repeatMode = repeatMode
            )
        }
    }

    private fun setCurrentDuration(currentDuration: Long) {
        _playbackState.update {
            it.copy(
                currentDuration = currentDuration
            )
        }
    }

    private fun resetLazyListState() {
        _lazyListState.value = LazyListState()
    }

    private fun onMusicChange() {
        viewModelScope.launch {
            val previousIndex = mediaController.previousMediaItemIndex
            val id: Long =
                mediaController.getMediaItemAt(previousIndex).mediaMetadata.description.toString()
                    .toLong()
            val musicFile = getMusicFileByIdUseCase(id) ?: return@launch
            val newMusicFile = musicFile.copy(
                playCount = musicFile.playCount + 1,
                datePlayed = System.currentTimeMillis()
            )
            setMusicDetails()
            modifyMusicStatusUseCase(newMusicFile)
        }
    }

    private fun setFavoriteState(isFavorite: Boolean) {
        var id: Long = -1
        mediaController.currentMediaItem?.mediaId?.let {
            id = it.toLong()
        }
        viewModelScope.launch(Dispatchers.IO) {
            updateMusicFavoriteState(id, isFavorite)
        }
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

    private fun updateTotalDuration(newDuration: Long) {
        if (newDuration >= 0) {
            _playbackState.update {
                it.copy(
                    totalDuration = newDuration
                )
            }
        }
    }

    private fun setSearchText(searchText: String) {
        _homeState.update {
            it.copy(
                searchFieldState = it.searchFieldState.copy(
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

    private fun setTitle(title: String) {
        _playbackState.update {
            it.copy(
                title = title
            )
        }
    }

    private fun setArtWork(artwork: Bitmap) {
        _playbackState.update {
            it.copy(
                bitmapBitmap = artwork
            )
        }
    }

    private fun setMusicDetails() {
        viewModelScope.launch {
            val id: Long = mediaController.currentMediaItem?.mediaId?.toLong() ?: -1
            val isFavorite = getMusicFileByIdUseCase(id)?.isFavorite ?: false
            _playbackState.update {
                it.copy(
                    isFavorite = isFavorite,
                    isShuffle = mediaController.shuffleModeEnabled,
                    artist = (mediaController.currentMediaItem?.mediaMetadata?.artist
                        ?: "Unknown_Artist").toString(),
                    title = (mediaController.currentMediaItem?.mediaMetadata?.title
                        ?: "Unknown_Title").toString(),
                    bitmapBitmap = uriToBitmap(
                        applicationContext,
                        mediaController.currentMediaItem?.mediaMetadata?.artworkUri
                    ),
                    repeatMode = when (mediaController.repeatMode) {
                        Player.REPEAT_MODE_ONE -> RepeatMode.Repeat_Once
                        Player.REPEAT_MODE_ALL -> RepeatMode.Repeat_All
                        Player.REPEAT_MODE_OFF -> RepeatMode.No_Repeat
                        else -> RepeatMode.No_Repeat
                    }
                )
            }

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