package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity

interface MusicQueueRepository {
    suspend fun insertMusic (musicQueueEntity: MusicQueueEntity)
    suspend fun updateMusic (musicQueueEntity: MusicQueueEntity)
    suspend fun deleteMusic (musicQueueEntity: MusicQueueEntity)
    suspend fun getAllMusic () : MutableList<MusicQueueEntity>
    suspend fun getMusicById(musicId: Long) : MusicQueueEntity?
    suspend fun deleteAllMusic()
}