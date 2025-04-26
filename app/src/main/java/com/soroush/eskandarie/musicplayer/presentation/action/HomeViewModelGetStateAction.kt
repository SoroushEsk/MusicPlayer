package com.soroush.eskandarie.musicplayer.presentation.action

import androidx.compose.foundation.lazy.LazyListState
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.presentation.state.HomeViewModelState


sealed class HomeViewModelGetStateAction  {
    data object GetSearchTextState : HomeViewModelGetStateAction()
    data object GetMusicFiles      : HomeViewModelGetStateAction()
    data object GetPlaylists       : HomeViewModelGetStateAction()
    data object GetMusicStatus     : HomeViewModelGetStateAction()
    data object GetLazyListState   : HomeViewModelGetStateAction()
    data object GetTopPlaylistState: HomeViewModelGetStateAction()
    data object GetFolderList      : HomeViewModelGetStateAction()
}