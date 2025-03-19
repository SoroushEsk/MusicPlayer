package com.soroush.eskandarie.musicplayer.domain.usecase.queue

import android.util.Log
import com.soroush.eskandarie.musicplayer.data.local.MusicEntity
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import javax.inject.Inject

class GetAllMusicOfQueueUseCase @Inject constructor(
    private val musicQueueRepository: MusicQueueRepository
) {
    suspend operator fun invoke() : List<MusicEntity> {
        return musicQueueRepository.getAllMusic()
    }
}