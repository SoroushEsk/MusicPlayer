package com.soroush.eskandarie.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.soroush.eskandarie.musicplayer.data.local.MusicEntity

@Dao
interface MusicQueueDao {
    @Insert
    fun insertMusic(musicEntity: MusicEntity)

    @Update
    fun updateMusic(musicEntity: MusicEntity)

    @Delete
    fun deleteMusic(musicEntity: MusicEntity)

    @Query("SELECT * FROM music_queue")
    fun getAllMusic(): MutableList<MusicEntity>

    @Query("Select * FROM music_queue WHERE id=:musicId")
    fun getMusicById(musicId: Long): MusicEntity?

    @Query("DELETE FROM music_queue")
    fun deleteAllMusic()


}