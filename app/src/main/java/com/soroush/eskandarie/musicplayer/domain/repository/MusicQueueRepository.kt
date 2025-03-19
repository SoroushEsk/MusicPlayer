package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.data.local.MusicEntity

interface MusicQueueRepository {
    suspend fun insertMusic (musicEntity: MusicEntity)
    suspend fun updateMusic (musicEntity: MusicEntity)
    suspend fun deleteMusic (musicEntity: MusicEntity)
    suspend fun getAllMusic () : MutableList<MusicEntity>
    suspend fun getMusicById(musicId: Long) : MusicEntity?
    suspend fun deleteAllMusic()
}