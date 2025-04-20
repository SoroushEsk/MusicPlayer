package com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music

import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistMusicRelationEntity
import com.soroush.eskandarie.musicplayer.domain.repository.MusicPlaylistRelationRepository
import javax.inject.Inject

class AddAMusicToPlaylistUseCase @Inject constructor(
    private val musicPlaylistRelationRepository: MusicPlaylistRelationRepository
) {
    suspend operator fun invoke(playlistMusicRelationEntity: PlaylistMusicRelationEntity) =
        musicPlaylistRelationRepository.insertMusicInPlaylist(playlistMusicRelationEntity)

}