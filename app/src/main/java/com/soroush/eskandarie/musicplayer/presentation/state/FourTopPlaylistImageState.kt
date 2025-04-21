package com.soroush.eskandarie.musicplayer.presentation.state

data class FourTopPlaylistImageState(
    val AllMusic: FourTopPlaylistImageState,
    val Folders: FourTopPlaylistImageState,
    val RecentrlyPlayed: FourTopPlaylistImageState,
    val MostPlayed: FourTopPlaylistImageState
)
