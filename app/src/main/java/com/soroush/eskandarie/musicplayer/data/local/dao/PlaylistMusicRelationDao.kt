package com.soroush.eskandarie.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistMusicRelationEntity
import com.soroush.eskandarie.musicplayer.util.Constants

@Dao
interface PlaylistMusicRelationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistMusicRelation(playlistMusicRelation: PlaylistMusicRelationEntity)

    @Update
    fun updatePlaylistMusicRelation(playlistMusicRelation: PlaylistMusicRelationEntity)

    @Delete
    fun deletePlaylistMusicRelation(playlistMusicRelation: PlaylistMusicRelationEntity)

    @Query("DELETE FROM ${Constants.Database.MusicPlaylistRelationTableName} WHERE ${Constants.Database.MusicPlaylistPlaylistIdColumn}=:playlistId")
    fun deletePlaylistById(playlistId: Long)


}