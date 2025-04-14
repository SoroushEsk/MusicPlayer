package com.soroush.eskandarie.musicplayer.domain.usecase.playlist

import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.repository.PlaylistRepository
import javax.inject.Inject

class CreateANewPlaylistUseCase  @Inject constructor(
    private val playlistRepository: PlaylistRepository
){
    suspend operator fun invoke(playlist: Playlist) =
        playlistRepository.addPlaylist(playlist)
}