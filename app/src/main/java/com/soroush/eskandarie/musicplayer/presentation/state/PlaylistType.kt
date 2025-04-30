package com.soroush.eskandarie.musicplayer.presentation.state
sealed class PlaylistType(name : String) {
    data class TopPlaylist(val route: String, val name: String)         : PlaylistType(name)
    data class FolderPlaylist(val folderName: String, val name: String) : PlaylistType(name)
    data class UserPlayList(val id: Long, val name: String)             : PlaylistType(name)
}