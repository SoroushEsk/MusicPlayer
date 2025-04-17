package com.soroush.eskandarie.musicplayer.domain.usecase.music

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import javax.inject.Inject

class ModifyMusicStatusUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(musicFile: MusicFile)=
        musicRepository.updateMusic(musicFile.toMusicEntity())
}