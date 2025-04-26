package com.soroush.eskandarie.musicplayer.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.util.Constants
import java.util.concurrent.Flow

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusic(musicEntity: MusicEntity)

    @Update
    fun updateMusic(musicEntity: MusicEntity)

    @Delete
    fun deleteMusic(musicEntity: MusicEntity)

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} ORDER BY ${Constants.Database.MusicIdColumn} ASC")
    fun getAllMusicPaging(): PagingSource<Int, MusicEntity>

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} ORDER BY ${Constants.Database.MusicIdColumn} ASC")
    fun getAllMusic(): List<MusicEntity>

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} WHERE ${Constants.Database.MusicIdColumn}=:musicId")
    fun getMusicById(musicId: Long): MusicEntity?

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} WHERE ${Constants.Database.MusicIsFavoriteColumn}=1 ORDER BY ${Constants.Database.MusicDatePlayedColumn} DESC")
    fun getFavoriteMusic(): PagingSource<Int, MusicEntity>

    @RawQuery
    fun getOrderedLimited(query: SupportSQLiteQuery): MutableList<MusicEntity>

}