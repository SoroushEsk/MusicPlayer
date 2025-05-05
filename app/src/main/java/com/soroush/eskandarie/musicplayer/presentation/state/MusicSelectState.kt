package com.soroush.eskandarie.musicplayer.presentation.state

data class MusicSelectState(
    val isSelectMode: Boolean = false,
    val selectedMusic: Map<Long, Boolean> = emptyMap()
)
