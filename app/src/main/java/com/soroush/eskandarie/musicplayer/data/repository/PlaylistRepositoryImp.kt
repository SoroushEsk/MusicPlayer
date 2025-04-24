package com.soroush.eskandarie.musicplayer.data.repository

import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistDao
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistRepositoryImp @Inject constructor(
    private val playlistDao: PlaylistDao
): PlaylistRepository {
    override suspend fun getAllPlaylists(): List<Playlist> {
        return withContext(Dispatchers.IO){
            val playlistEntityList = playlistDao.getAllPlaylists()
            playlistEntityList.map{
                it.toPlaylist()
            }
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO){
            playlistDao.insertPlaylist(playlist.toPlaylistEntity())
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist){
        withContext(Dispatchers.IO){
            playlistDao.deletePlaylist(playlist.toPlaylistEntity())
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist){
        withContext(Dispatchers.IO){
            playlistDao.updatePlaylist(playlist.toPlaylistEntity())
        }
    }

    override suspend fun getNumberOfPlaylist(): Int {
        return withContext(Dispatchers.IO){
            playlistDao.getTotalPlaylist()
        }
    }


}