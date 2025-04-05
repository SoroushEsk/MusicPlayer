package com.soroush.eskandarie.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistEntity
import com.soroush.eskandarie.musicplayer.util.Constants

@Dao
interface PlaylistDao {
    @Insert
    fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Update
    fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Delete
    fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM ${Constants.Database.PlaylistTableName}")
    fun getAllPlaylists(): MutableList<PlaylistEntity>

    @Query("DELETE FROM ${Constants.Database.PlaylistTableName}")
    fun deleteAllPlaylists()


}