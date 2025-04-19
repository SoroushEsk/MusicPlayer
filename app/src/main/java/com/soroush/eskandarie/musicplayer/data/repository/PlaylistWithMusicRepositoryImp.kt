package com.soroush.eskandarie.musicplayer.data.repository

import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistWithMusicDao
import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistWithMusic
import com.soroush.eskandarie.musicplayer.domain.repository.PlaylistWithMusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistWithMusicRepositoryImp @Inject constructor(
    private val playlistWithMusicDao: PlaylistWithMusicDao
): PlaylistWithMusicRepository {
    override suspend fun getPlaylistAllMusicById(playlistId: Long): PlaylistWithMusic {
        return withContext(Dispatchers.IO){
            playlistWithMusicDao.getPlaylistWithMusic(playlistId)
        }
    }
}