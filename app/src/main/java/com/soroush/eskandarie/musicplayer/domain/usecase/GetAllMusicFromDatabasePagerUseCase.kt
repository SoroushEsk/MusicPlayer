package com.soroush.eskandarie.musicplayer.domain.usecase

import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import javax.inject.Inject

class GetAllMusicFromDatabasePagerUseCase @Inject constructor(
    private val musicRepository : MusicRepository
) {
    suspend operator fun invoke() = musicRepository.getAllMusicFilesPager()
}