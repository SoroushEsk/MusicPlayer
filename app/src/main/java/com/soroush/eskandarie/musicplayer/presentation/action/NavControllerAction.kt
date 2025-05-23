package com.soroush.eskandarie.musicplayer.presentation.action

sealed class NavControllerAction(route: String) {
    data class NavigateToPlaylist(val playlistId: Long, val route: String, val name: String) : NavControllerAction(route)
    data class NavigateToAllMusic(val route: String) : NavControllerAction(route)
    data class NavigateToRecentlyPlayed(val route: String) : NavControllerAction(route)
    data class NavigateToMostPlayed(val route: String) : NavControllerAction(route)
    data class NavigateToFolders(val route: String): NavControllerAction(route)
    data class NavigateToFavorite(val route: String): NavControllerAction(route)
    data class NavigateToFolderMusic(val route: String, val folderName: String): NavControllerAction(route)
}