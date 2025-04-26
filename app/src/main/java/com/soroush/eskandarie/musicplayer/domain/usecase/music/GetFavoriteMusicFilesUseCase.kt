package com.soroush.eskandarie.musicplayer.domain.usecase.music

import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import javax.inject.Inject

class GetFavoriteMusicFilesUseCase @Inject constructor(
    private val musicRepository: MusicRepository
){
    suspend operator fun invoke() = musicRepository.getFavoriteMusicFiles()
}