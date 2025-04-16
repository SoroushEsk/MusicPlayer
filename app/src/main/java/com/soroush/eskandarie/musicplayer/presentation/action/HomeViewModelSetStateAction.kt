package com.soroush.eskandarie.musicplayer.presentation.action

sealed class HomeViewModelSetStateAction {
    data class SetStateSearchTextHome(val searchText: String)   : HomeViewModelSetStateAction()
    data class SetCurrentPlaylistName(val playlistName: String) : HomeViewModelSetStateAction()
    data object GetAllMusicFiles                                : HomeViewModelSetStateAction()
    data object GetAllPlaylists                                 : HomeViewModelSetStateAction()
    data object SetStateSongPercentHome                         : HomeViewModelSetStateAction()
}