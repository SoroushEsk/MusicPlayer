package com.soroush.eskandarie.musicplayer.data.repository


import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SupportSQLiteQuery
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicDao
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDeviceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepositoryImp @Inject constructor(
    private val getAllMusicFromDeviceUseCase: GetAllMusicFromDeviceUseCase,
    private val musicTableDao: MusicDao
) : MusicRepository {
    //region Overrides Methods
    override suspend fun saveDeviceMusicFile() {
        withContext(Dispatchers.IO) {
            getAllMusicFromDeviceUseCase().collect { musicList ->
                musicList.forEach { musicFile ->
                    val musicEntity = MusicEntity(
                        id = musicFile.id,
                        artist = musicFile.artist,
                        title = musicFile.title,
                        path = musicFile.path,
                        posterPath = "",
                        isFavorite = false,
                        playCount = 0L,
                        duration = musicFile.duration,
                        datePlayed = System.currentTimeMillis()
                    )
                    Log.e("MusicItemArtistTextEndPadding", musicFile.toString())
                    musicTableDao.insertMusic(musicEntity)
                }
            }
        }
    }
    override suspend fun getAllMusicFilesPager(): Flow<PagingData<MusicFile>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            musicTableDao.getAllMusicPaging()
        }
    ).flow
        .map { pagingData: PagingData<MusicEntity> ->
            pagingData.map { entity ->
                entity.toMusicFile()
            }
        }
    override suspend fun updateMusic(musicEntity: MusicEntity) = withContext(Dispatchers.IO) {
        musicTableDao.updateMusic(musicEntity)
    }
    override suspend fun getMusicFileById(musicId: Long): MusicFile? = withContext(Dispatchers.IO) {
        musicTableDao.getMusicById(musicId)?.toMusicFile()
    }
    override suspend fun getOrderedMusicList(
        query: SupportSQLiteQuery
    ): List<MusicFile> = withContext(Dispatchers.IO){
        musicTableDao.getOrderedLimited(query).map{
            it.toMusicFile()
        }
    }
    override suspend fun getFavoriteMusicFilesPager(): Flow<PagingData<MusicFile>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                musicTableDao.getFavoriteMusicPaging()
            }
        ).flow.map {
            it.map { entity ->
                entity.toMusicFile()
            }
        }
    }
    override suspend fun getAllMusicFolder(): Map<String, List<MusicFile>> {
        return withContext(Dispatchers.IO){
            val map = mutableMapOf<String, List<MusicFile>>()
            musicTableDao.getAllMusic().forEach{musicEntity ->
                val parentFolder = getMediaParentFolder(musicEntity.path)
                map[parentFolder] = map.getOrDefault(parentFolder, emptyList()) + musicEntity.toMusicFile()
            }
            map
        }
    }

    override suspend fun getAllMusic(): List<MusicFile> =
        withContext(Dispatchers.IO){
            musicTableDao.getAllMusic().map{
                it.toMusicFile()
            }
        }

    override suspend fun getAllFavorite(): List<MusicFile> =
        withContext(Dispatchers.IO){
            musicTableDao.getFavoriteMusic().map { it.toMusicFile() }
        }

    override suspend fun updateFavoriteById(isFavorite: Boolean, musicId: Long) =
        withContext(Dispatchers.IO){
            musicTableDao.updtateFavorite(isFavorite, musicId)
        }
    //endregion
    //region class functions
    private fun getMediaParentFolder(path: String): String {
        val file = java.io.File(path)
        return file.parentFile?.name ?: "Unidentified"
    }
    //endregion
}