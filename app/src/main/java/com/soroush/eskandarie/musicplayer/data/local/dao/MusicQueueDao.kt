package com.soroush.eskandarie.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity

@Dao
interface MusicQueueDao {
    @Insert
    fun insertMusic(musicQueueEntity: MusicQueueEntity)

    @Update
    fun updateMusic(musicQueueEntity: MusicQueueEntity)

    @Delete
    fun deleteMusic(musicQueueEntity: MusicQueueEntity)

    @Query("SELECT * FROM music_queue")
    fun getAllMusic(): MutableList<MusicQueueEntity>

    @Query("Select * FROM music_queue WHERE id=:musicId")
    fun getMusicById(musicId: Long): MusicQueueEntity?

    @Query("DELETE FROM music_queue")
    fun deleteAllMusic()


}