package com.soroush.eskandarie.musicplayer.domain.usecase

import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import javax.inject.Inject

class GetAllMusicFromDatabaseUseCase @Inject constructor(
    private val musicRepository : MusicRepository
) {
     operator fun invoke() = musicRepository.getAllMusicFiles()
}