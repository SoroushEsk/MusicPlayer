package com.soroush.eskandarie.musicplayer.data.repository

import android.util.Log
import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistMusicRelationDao
import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistMusicRelationEntity
import com.soroush.eskandarie.musicplayer.domain.repository.MusicPlaylistRelationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicPlaylistRelationRepositoryImp @Inject constructor(
    private val playlistMusicRelationDao: PlaylistMusicRelationDao
): MusicPlaylistRelationRepository {
    override suspend fun insertMusicInPlaylist(musicPlaylistEntity: PlaylistMusicRelationEntity) =
        withContext(Dispatchers.IO){
            Log.e("123455", "insertMusicInPlaylist: $musicPlaylistEntity")
            playlistMusicRelationDao.insertPlaylistMusicRelation(musicPlaylistEntity)
        }
    override suspend fun insertMusicListInPlaylist(musicList: List<PlaylistMusicRelationEntity>) =
        withContext(Dispatchers.IO){
            musicList.forEach{
                playlistMusicRelationDao.insertPlaylistMusicRelation(it)
            }
        }
    override suspend fun deleteMusicFromPlaylist(musicPlaylistEntity: PlaylistMusicRelationEntity) =
        withContext(Dispatchers.IO){
            playlistMusicRelationDao.deletePlaylistMusicRelation(musicPlaylistEntity)
        }
    override suspend fun deleteAllMusicFromPlaylist(musicPlaylistEntity: PlaylistMusicRelationEntity) =
        withContext(Dispatchers.IO){
            playlistMusicRelationDao.deletePlaylistById(musicPlaylistEntity.playlistId)
        }

}