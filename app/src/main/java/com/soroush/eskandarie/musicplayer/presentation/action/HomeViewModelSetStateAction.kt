package com.soroush.eskandarie.musicplayer.presentation.action

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode

sealed class HomeViewModelSetStateAction {
    data class SetStateSearchText(val searchText: String)       : HomeViewModelSetStateAction()
    data class SetCurrentPlaylistName(val playlistName: String) : HomeViewModelSetStateAction()
    data class SetPlayState(val isMusicPlaying: Boolean)        : HomeViewModelSetStateAction()
    data class SetShuffleState(val isShuffle: Boolean)          : HomeViewModelSetStateAction()
    data class SetRepeatMode(val repeatMode: RepeatMode)        : HomeViewModelSetStateAction()
    data class SetCurrentDuration(val currentDuration: Long)    : HomeViewModelSetStateAction()
    data class OnNextMusic(val nextMusic: Long)            : HomeViewModelSetStateAction()
    data class ChangeFavoriteState(val isFavorite: Boolean)     : HomeViewModelSetStateAction()
    data object UpdatePlayCount                                 : HomeViewModelSetStateAction()
    data object UpdateDatePlayed                                : HomeViewModelSetStateAction()
    data object SetMusicPercent                                 : HomeViewModelSetStateAction()
    data object GetAllMusicFiles                                : HomeViewModelSetStateAction()
    data object GetAllPlaylists                                 : HomeViewModelSetStateAction()
}
