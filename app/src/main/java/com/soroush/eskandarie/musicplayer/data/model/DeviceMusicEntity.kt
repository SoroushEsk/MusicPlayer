package com.soroush.eskandarie.musicplayer.data.model

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile

data class DeviceMusicEntity(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val recordingDate: String?,
    val genre: String?,
    val size: Long,
    val path: String
) {
    fun toMusic() = MusicFile(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        recordingDate = recordingDate,
        genre = genre,
        size = size,
        path = path,
        isFavorite = false,
        datePlayed = 0,
        playCount = 0
    )
}