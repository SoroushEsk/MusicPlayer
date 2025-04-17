package com.soroush.eskandarie.musicplayer.presentation.state

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile

data class PlaybackStates(
    val isPlaying : Boolean = false,
    val isShuffle : Boolean = false,
    val currentDuration: Long = 0,
    val musicPercent: Float = 0.0f,
    val repeatMode: RepeatMode = RepeatMode.No_Repeat,
    val currentMusicFile: MusicFile = MusicFile(
        id = -1,
        title = "",
        album = "",
        artist = "",
        path = "",
        duration = 0,
        isFavorite = false,
        playCount = 0,
        datePlayed = 0
    )
)
