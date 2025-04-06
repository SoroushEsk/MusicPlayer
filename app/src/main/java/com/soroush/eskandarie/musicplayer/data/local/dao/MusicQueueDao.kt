package com.soroush.eskandarie.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity
import com.soroush.eskandarie.musicplayer.util.Constants

@Dao
interface MusicQueueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusic(musicQueueEntity: MusicQueueEntity)

    @Update
    fun updateMusic(musicQueueEntity: MusicQueueEntity)

    @Delete
    fun deleteMusic(musicQueueEntity: MusicQueueEntity)

    @Query("SELECT * FROM ${Constants.Database.MusicQueueTableName}")
    fun getAllMusic(): MutableList<MusicQueueEntity>

    @Query("Select * FROM ${Constants.Database.MusicQueueTableName} WHERE ${Constants.Database.MusicQueueIdColumn}=:musicId")
    fun getMusicById(musicId: Long): MusicQueueEntity?

    @Query("DELETE FROM ${Constants.Database.MusicQueueTableName}")
    fun deleteAllMusic()


}