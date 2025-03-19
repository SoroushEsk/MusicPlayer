package com.soroush.eskandarie.musicplayer.domain.usecase.queue

import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import javax.inject.Inject

class GetMusicFromQueueUseCase @Inject constructor(
    private val musicQueueRepository: MusicQueueRepository
) {
    suspend operator fun invoke(musicId: Long) = musicQueueRepository.getMusicById(musicId)
}