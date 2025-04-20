package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistMusicRelationEntity

interface MusicPlaylistRelationRepository {
    suspend fun insertMusicInPlaylist(musicPlaylistEntity: PlaylistMusicRelationEntity)
    suspend fun insertMusicListInPlaylist(musicList: List<PlaylistMusicRelationEntity>)
    suspend fun deleteMusicFromPlaylist(musicPlaylistEntity: PlaylistMusicRelationEntity)
    suspend fun deleteAllMusicFromPlaylist(musicPlaylistEntity: PlaylistMusicRelationEntity)
}