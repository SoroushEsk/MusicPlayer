package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile

interface MusicRepository {
    suspend fun saveDeviceMusicFile( )
    suspend fun getAllMusicFiles(): List<MusicFile>
    suspend fun updateMusic(musicEntity: MusicEntity)
}