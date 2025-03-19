package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import kotlinx.coroutines.flow.Flow

interface DeviceMusicRepository {
    suspend fun getAllMusicFiles() : Flow<List<MusicFile>>
    suspend fun getMusicFile()
}