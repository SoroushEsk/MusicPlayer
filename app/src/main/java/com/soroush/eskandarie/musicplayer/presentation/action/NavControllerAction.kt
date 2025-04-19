package com.soroush.eskandarie.musicplayer.presentation.action

sealed class NavControllerAction(route: String) {
    data class NavigateToPlaylist(val playlistId: Long, val route: String) : NavControllerAction(route)
    data class NavitateToAllMusic(val route: String) : NavControllerAction(route)
}