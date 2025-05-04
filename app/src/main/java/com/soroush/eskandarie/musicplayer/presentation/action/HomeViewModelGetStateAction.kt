package com.soroush.eskandarie.musicplayer.presentation.action


sealed class HomeViewModelGetStateAction  {
    data object GetSearchTextState : HomeViewModelGetStateAction()
    data object GetOnQueuePlaylist : HomeViewModelGetStateAction()
    data object GetPlaylists       : HomeViewModelGetStateAction()
    data object GetMusicStatus     : HomeViewModelGetStateAction()
    data object GetLazyListState   : HomeViewModelGetStateAction()
    data object GetTopPlaylistState: HomeViewModelGetStateAction()
    data object GetFolderList      : HomeViewModelGetStateAction()
    data object GetCurrentPlaylist : HomeViewModelGetStateAction()
}