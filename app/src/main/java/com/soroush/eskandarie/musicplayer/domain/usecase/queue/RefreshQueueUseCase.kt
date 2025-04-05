package com.soroush.eskandarie.musicplayer.domain.usecase.queue

import android.util.Log
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import javax.inject.Inject

class RefreshQueueUseCase @Inject constructor(
    private val musicQueueRepository: MusicQueueRepository
) {
    suspend operator fun invoke(musicList: List<MusicQueueEntity>){
        musicQueueRepository.deleteAllMusic()
        Log.e("sth is wrong", "${musicList.size}")
        musicList.forEach{song ->
            musicQueueRepository.insertMusic(song)
        }
    }
}