package com.soroush.eskandarie.musicplayer.domain.usecase

import com.soroush.eskandarie.musicplayer.domain.repository.PlaylistWithMusicRepository
import javax.inject.Inject

class GetPlaylistWithAllMusicFileByIdUseCase @Inject constructor(
    private val playlistWithMusicRepository: PlaylistWithMusicRepository
) {
    suspend operator fun invoke(playlistId: Long) = playlistWithMusicRepository.getPlaylistAllMusicById(playlistId)
}