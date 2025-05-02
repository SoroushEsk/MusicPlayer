package com.soroush.eskandarie.musicplayer.domain.repository

import androidx.paging.PagingData
import androidx.sqlite.db.SupportSQLiteQuery
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun saveDeviceMusicFile( )
    suspend fun getAllMusicFilesPager(): Flow<PagingData<MusicFile>>
    suspend fun updateMusic(musicEntity: MusicEntity)
    suspend fun getMusicFileById(musicId: Long): MusicFile?
    suspend fun getOrderedMusicList(query: SupportSQLiteQuery): List<MusicFile>
    suspend fun getFavoriteMusicFilesPager(): Flow<PagingData<MusicFile>>
    suspend fun getAllMusicFolder(): Map<String, List<MusicFile>>
    suspend fun getAllMusic():  List<MusicFile>
    suspend fun getAllFavorite(): List<MusicFile>
    suspend fun updateFavoriteById(isFavorite: Boolean, musicId: Long)
}