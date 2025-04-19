package com.soroush.eskandarie.musicplayer.domain.repository

import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistWithMusic
import com.soroush.eskandarie.musicplayer.domain.model.Playlist

interface PlaylistWithMusicRepository {
    suspend fun getPlaylistAllMusicById(playlistId: Long): PlaylistWithMusic
}