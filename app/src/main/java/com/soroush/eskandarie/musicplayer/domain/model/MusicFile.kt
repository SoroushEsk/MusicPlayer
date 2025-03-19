package com.soroush.eskandarie.musicplayer.domain.model

data class MusicFile(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val recordingDate: String?,
    val genre: String?,
    val size: Long,
    val path: String
)