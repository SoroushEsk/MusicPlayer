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

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusic(musicEntity: MusicEntity)

    @Update
    fun updateMusic(musicEntity: MusicEntity)

    @Delete
    fun deleteMusic(musicEntity: MusicEntity)

    @Query("UPDATE ${Constants.Database.MusicTableName} SET ${Constants.Database.MusicQueueIsFavoriteColumn}=:isFavorite WHERE ${Constants.Database.MusicIdColumn} =:musicId")
    fun updtateFavorite(isFavorite: Boolean, musicId: Long)

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} ORDER BY ${Constants.Database.MusicIdColumn} ASC")
    fun getAllMusicPaging(): PagingSource<Int, MusicEntity>

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} ORDER BY ${Constants.Database.MusicIdColumn} ASC")
    fun getAllMusic(): List<MusicEntity>

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} WHERE ${Constants.Database.MusicIdColumn}=:musicId")
    fun getMusicById(musicId: Long): MusicEntity?

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} WHERE ${Constants.Database.MusicIsFavoriteColumn}=1 ORDER BY ${Constants.Database.MusicDatePlayedColumn} DESC")
    fun getFavoriteMusicPaging(): PagingSource<Int, MusicEntity>

    @Query("SELECT * FROM ${Constants.Database.MusicTableName} WHERE ${Constants.Database.MusicIsFavoriteColumn}=1 ORDER BY ${Constants.Database.MusicDatePlayedColumn} DESC")
    fun getFavoriteMusic(): List<MusicEntity>

    @RawQuery
    fun getOrderedLimited(query: SupportSQLiteQuery): MutableList<MusicEntity>

}