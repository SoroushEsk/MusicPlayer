package com.soroush.eskandarie.musicplayer.domain.usecase.queue

import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import javax.inject.Inject

class GetAllMusicOfQueueUseCase @Inject constructor(
    private val musicQueueRepository: MusicQueueRepository
) {
    suspend operator fun invoke() : List<MusicQueueEntity> {
        return musicQueueRepository.getAllMusic()
    }
}