package com.soroush.eskandarie.musicplayer.data.repository

import com.soroush.eskandarie.musicplayer.data.local.MusicEntity
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicQueueRepositoryImpl @Inject constructor(
    private val musicQueueDao: MusicQueueDao): MusicQueueRepository {

    override suspend fun insertMusic(musicEntity: MusicEntity) {
        withContext(Dispatchers.IO){
            musicQueueDao.insertMusic(musicEntity)
        }
    }

    override suspend fun updateMusic(musicEntity: MusicEntity) {
        withContext(Dispatchers.IO){
            musicQueueDao.updateMusic(musicEntity)
        }
    }

    override suspend fun deleteMusic(musicEntity: MusicEntity) {
        withContext(Dispatchers.IO){
            musicQueueDao.deleteMusic(musicEntity)
        }
    }

    override suspend fun getAllMusic(): MutableList<MusicEntity> =
        withContext(Dispatchers.IO){
            musicQueueDao.getAllMusic()
        }

    override suspend fun getMusicById(musicId: Long): MusicEntity? =
        withContext(Dispatchers.IO){
            musicQueueDao.getMusicById(musicId)
        }

    override suspend fun deleteAllMusic() {
        withContext(Dispatchers.IO){
            musicQueueDao.deleteAllMusic()
        }
    }
}