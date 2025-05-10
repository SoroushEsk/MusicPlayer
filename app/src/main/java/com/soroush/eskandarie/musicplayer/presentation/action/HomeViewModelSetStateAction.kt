package com.soroush.eskandarie.musicplayer.presentation.action

import android.graphics.Bitmap
import androidx.media3.session.MediaController
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.presentation.state.PlaylistType
import com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode

sealed class HomeViewModelSetStateAction {
    data class SetUpMusicList(
        val folderName: String = "",
        val id: Long = -1,
        val route: String
    )                                                           : HomeViewModelSetStateAction()
    data class SetSongToPlay(
        val playlistType: PlaylistType,
        val id: Long
    )                                                           : HomeViewModelSetStateAction()
    data class AddMusicListToPlaylist(
        val playlistId: Long,
        val musicList: List<MusicFile>
    )                                                           : HomeViewModelSetStateAction()
    data class SetMediaControllerObserver(
        val mediaController : MediaController
    )                                                           : HomeViewModelSetStateAction()
    data class SetStateSearchText(val searchText: String)       : HomeViewModelSetStateAction()
    data class SetPlayState(val isMusicPlaying: Boolean)        : HomeViewModelSetStateAction()
    data class SetShuffleState(val isShuffle: Boolean)          : HomeViewModelSetStateAction()
    data class SetRepeatMode(val repeatMode: RepeatMode)        : HomeViewModelSetStateAction()
    data class SetCurrentDuration(val currentDuration: Long)    : HomeViewModelSetStateAction()
    data class ChangeFavoriteState(val isFavorite: Boolean)     : HomeViewModelSetStateAction()
    data class UpdateArtist(val artist: String)                 : HomeViewModelSetStateAction()
    data class UpdateTitle(val title: String)                   : HomeViewModelSetStateAction()
    data class UpdateArtWork(val artWork: Bitmap)               : HomeViewModelSetStateAction()
    data class AddMusicToPlaylist(val musicId: Long)            : HomeViewModelSetStateAction()
    data class AddANewPlaylist(val name: String)                : HomeViewModelSetStateAction()
    data class PutPlaylistToQueue(val playlistType:PlaylistType): HomeViewModelSetStateAction()
    data class SetCurrentPlaylist(val playlist: PlaylistType)   : HomeViewModelSetStateAction()
    data object UpdateTopPlaylistState                          : HomeViewModelSetStateAction()
    data object OnNextMusic                                     : HomeViewModelSetStateAction()
    data object FillFolderRequirements                          : HomeViewModelSetStateAction()
    data object ResetLazyListState                              : HomeViewModelSetStateAction()
    data object UpdateMusicDetails                              : HomeViewModelSetStateAction()
    data object SetMusicPercent                                 : HomeViewModelSetStateAction()
    data object GetAllMusicFiles                                : HomeViewModelSetStateAction()
    data object GetAllPlaylists                                 : HomeViewModelSetStateAction()
    data object PausePlayback                                   : HomeViewModelSetStateAction()
    data object ResumePlayback                                  : HomeViewModelSetStateAction()
    data object ForwardPlayback                                 : HomeViewModelSetStateAction()
    data object BackwardPlayback                                : HomeViewModelSetStateAction()
}