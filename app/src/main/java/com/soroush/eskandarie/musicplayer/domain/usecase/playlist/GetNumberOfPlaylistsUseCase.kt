package com.soroush.eskandarie.musicplayer.domain.usecase.playlist

import com.soroush.eskandarie.musicplayer.domain.repository.PlaylistRepository
import javax.inject.Inject

class GetNumberOfPlaylistsUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(): Int {
        return playlistRepository.getNumberOfPlaylist()
    }

}