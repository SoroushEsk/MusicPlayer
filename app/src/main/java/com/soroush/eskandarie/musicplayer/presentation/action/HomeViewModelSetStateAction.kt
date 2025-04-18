package com.soroush.eskandarie.musicplayer.presentation.action

import android.graphics.Bitmap
import androidx.media3.session.MediaController
import com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode

sealed class HomeViewModelSetStateAction {
    data class SetMediaControllerObserver(
        val mediaController : MediaController
    )                                                           : HomeViewModelSetStateAction()
    data class SetStateSearchText(val searchText: String)       : HomeViewModelSetStateAction()
    data class SetCurrentPlaylistName(val playlistName: String) : HomeViewModelSetStateAction()
    data class SetPlayState(val isMusicPlaying: Boolean)        : HomeViewModelSetStateAction()
    data class SetShuffleState(val isShuffle: Boolean)          : HomeViewModelSetStateAction()
    data class SetRepeatMode(val repeatMode: RepeatMode)        : HomeViewModelSetStateAction()
    data class SetCurrentDuration(val currentDuration: Long)    : HomeViewModelSetStateAction()
    data class OnNextMusic(val nextMusic: Long)                 : HomeViewModelSetStateAction()
    data class ChangeFavoriteState(val isFavorite: Boolean)     : HomeViewModelSetStateAction()
    data class UpdateArtist(val artist: String)                 : HomeViewModelSetStateAction()
    data class UpdateTitle(val title: String)                   : HomeViewModelSetStateAction()
    data class UpdateArtWork(val artWork: Bitmap)               : HomeViewModelSetStateAction()
    data class SetUpMusicList(val id: Long = -1, val route: String)  : HomeViewModelSetStateAction()
    data object ResetLazyListState                              : HomeViewModelSetStateAction()
    data object UpdateMusicDetails                              : HomeViewModelSetStateAction()
    data object UpdatePlayCount                                 : HomeViewModelSetStateAction()
    data object UpdateDatePlayed                                : HomeViewModelSetStateAction()
    data object SetMusicPercent                                 : HomeViewModelSetStateAction()
    data object GetAllMusicFiles                                : HomeViewModelSetStateAction()
    data object GetAllPlaylists                                 : HomeViewModelSetStateAction()
    data object PausePlayback                                   : HomeViewModelSetStateAction()
    data object ResumePlayback                                  : HomeViewModelSetStateAction()
    data object ForwardPlayback                                 : HomeViewModelSetStateAction()
    data object BackwardPlayback                                : HomeViewModelSetStateAction()
}