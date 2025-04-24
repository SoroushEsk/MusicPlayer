package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.domain.model.Playlist

interface PlaylistRepository {
    suspend fun getAllPlaylists(): List<Playlist>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getNumberOfPlaylist(): Int
}