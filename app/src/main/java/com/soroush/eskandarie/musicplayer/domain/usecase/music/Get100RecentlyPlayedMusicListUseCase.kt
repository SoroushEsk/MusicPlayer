package com.soroush.eskandarie.musicplayer.domain.usecase.music

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import com.soroush.eskandarie.musicplayer.util.Constants
import javax.inject.Inject

class Get100RecentlyPlayedMusicListUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): List<MusicFile> {
        return musicRepository.getOrderedMusicList(Constants.Database.MusicDatePlayedColumn, 100)
    }

}