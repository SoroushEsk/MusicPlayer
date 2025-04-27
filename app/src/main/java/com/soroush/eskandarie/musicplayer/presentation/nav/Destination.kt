package com.soroush.eskandarie.musicplayer.presentation.nav

sealed class Destination(val route: String) {
    data object HomeScreen: Destination("home_screen")
    data object MostPlayedScreen: Destination("most_played_screen")
    data object RecentlyPlayedScreen: Destination("recently_played_screen")
    data object FolderScreen: Destination("folder_screen")
    data object AllMusicScreen: Destination("all_music_screen")
    data object FavoriteMusicScreen: Destination("favorite_music_screen")
    data object PlaylistScreen: Destination("playlist_screen")
    data object FolderMusicScreen: Destination("folder_music_screen")
}