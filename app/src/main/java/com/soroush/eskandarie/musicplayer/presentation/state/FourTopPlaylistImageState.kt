package com.soroush.eskandarie.musicplayer.presentation.state

data class FourTopPlaylistImageState(
    val AllMusic: TopPlaylistState = TopPlaylistState(),
    val RecentrlyPlayed: TopPlaylistState = TopPlaylistState(),
    val MostPlayed: TopPlaylistState = TopPlaylistState()
)
