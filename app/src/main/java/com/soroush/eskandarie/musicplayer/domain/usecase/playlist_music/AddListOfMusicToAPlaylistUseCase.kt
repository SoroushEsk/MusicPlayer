package com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music

import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistMusicRelationEntity
import com.soroush.eskandarie.musicplayer.domain.repository.MusicPlaylistRelationRepository
import javax.inject.Inject

class AddListOfMusicToAPlaylistUseCase @Inject constructor(
    private val musicPlaylistRelationRepository: MusicPlaylistRelationRepository
) {
    suspend operator fun invoke(list: List<PlaylistMusicRelationEntity>) =
        musicPlaylistRelationRepository.insertMusicListInPlaylist(list)
}