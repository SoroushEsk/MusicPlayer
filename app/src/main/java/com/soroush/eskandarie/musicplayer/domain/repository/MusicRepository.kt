package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile

interface MusicRepository {
    suspend fun saveDeviceMusicFile( )
}