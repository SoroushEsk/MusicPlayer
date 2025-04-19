package com.soroush.eskandarie.musicplayer.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun saveDeviceMusicFile( )
    suspend fun getAllMusicFiles(): Flow<PagingData<MusicFile>>
    suspend fun updateMusic(musicEntity: MusicEntity)
    suspend fun getMusicFileById(musicId: Long): MusicFile?
}