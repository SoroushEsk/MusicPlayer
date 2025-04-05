package com.soroush.eskandarie.musicplayer.data.repository

import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicQueueRepositoryImpl @Inject constructor(
    private val musicQueueDao: MusicQueueDao): MusicQueueRepository {

    override suspend fun insertMusic(musicQueueEntity: MusicQueueEntity) {
        withContext(Dispatchers.IO){
            musicQueueDao.insertMusic(musicQueueEntity)
        }
    }

    override suspend fun updateMusic(musicQueueEntity: MusicQueueEntity) {
        withContext(Dispatchers.IO){
            musicQueueDao.updateMusic(musicQueueEntity)
        }
    }

    override suspend fun deleteMusic(musicQueueEntity: MusicQueueEntity) {
        withContext(Dispatchers.IO){
            musicQueueDao.deleteMusic(musicQueueEntity)
        }
    }

    override suspend fun getAllMusic(): MutableList<MusicQueueEntity> =
        withContext(Dispatchers.IO){
            musicQueueDao.getAllMusic()
        }

    override suspend fun getMusicById(musicId: Long): MusicQueueEntity? =
        withContext(Dispatchers.IO){
            musicQueueDao.getMusicById(musicId)
        }

    override suspend fun deleteAllMusic() {
        withContext(Dispatchers.IO){
            musicQueueDao.deleteAllMusic()
        }
    }
}