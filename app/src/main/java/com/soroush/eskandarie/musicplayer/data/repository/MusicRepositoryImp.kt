package com.soroush.eskandarie.musicplayer.data.repository

import android.util.Log
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicDao
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDeviceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepositoryImp @Inject constructor(
    private val getAllMusicFromDeviceUseCase: GetAllMusicFromDeviceUseCase,
    private val musicTableDao: MusicDao
): MusicRepository{
    override suspend fun saveDeviceMusicFile() {
        withContext(Dispatchers.IO){
            getAllMusicFromDeviceUseCase().collect{ musicList ->
                musicList.forEach{ musicFile ->
                    val musicEntity = MusicEntity(
                        id = musicFile.id,
                        artist = musicFile.artist,
                        title = musicFile.title,
                        path = musicFile.path,
                        posterPath = "",
                        isFavorite = false,
                        playCount = 0L,
                        duration = musicFile.duration,
                        datePlayed = System.currentTimeMillis()
                    )
                    musicTableDao.insertMusic(musicEntity)
                }
            }
        }
    }
}