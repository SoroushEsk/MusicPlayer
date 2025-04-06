package com.soroush.eskandarie.musicplayer.data.repository

import android.content.Context
import android.provider.MediaStore
import com.soroush.eskandarie.musicplayer.data.model.DeviceMusicEntity
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.repository.DeviceMusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceMusicRepositoryImpl @Inject constructor(private val context : Context):

    DeviceMusicRepository{
    override suspend fun getAllMusicFiles(): Flow<List<MusicFile>> = flow {
        val musicList = mutableListOf<DeviceMusicEntity>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.GENRE,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATA
        )

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val genreColumn = cursor.getColumnIndex(MediaStore.Audio.Media.GENRE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val music = DeviceMusicEntity(
                    id = cursor.getLong(idColumn),
                    title = cursor.getString(titleColumn) ?: "Unknown Title",
                    artist = cursor.getString(artistColumn) ?: "Unknown Artist",
                    album = cursor.getString(albumColumn) ?: "Unknown Album",
                    duration = cursor.getLong(durationColumn),
                    recordingDate = cursor.getString(dateColumn).takeIf { it != null },
                    genre = if (genreColumn != -1) cursor.getString(genreColumn) else null,
                    size = cursor.getLong(sizeColumn),
                    path = cursor.getString(pathColumn) ?: ""
                )
                musicList.add(music)
            }
        }

        emit(musicList.map { it.toMusic() })
    }.flowOn(Dispatchers.IO)

    override suspend fun getMusicFile() {
        TODO("Not yet implemented")
    }
}