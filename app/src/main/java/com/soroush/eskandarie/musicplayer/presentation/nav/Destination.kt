package com.soroush.eskandarie.musicplayer.presentation.nav

sealed class Destination(val route: String) {
    object HomeScreen: Destination("home_screen")
    object MostPlayedScreen: Destination("most_played_screen")
    object RecentlyPlayedScreen: Destination("recently_played_screen")
    object FolderScreen: Destination("folder_screen")
    object AllMusicScreen: Destination("all_music_screen")
    object FavoriteMusicScreen: Destination("favorite_music_screen")
    object PlaylistScreen: Destination("playlist_screen")
}