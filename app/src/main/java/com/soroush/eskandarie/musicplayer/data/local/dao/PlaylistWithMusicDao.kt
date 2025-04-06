package com.soroush.eskandarie.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistWithMusic
import com.soroush.eskandarie.musicplayer.util.Constants

@Dao
interface PlaylistWithMusicDao {
    @Transaction
    @Query("SELECT * FROM ${Constants.Database.PlaylistTableName} WHERE ${Constants.Database.PlaylistIdColumn}=:playlistId")
    fun getPlaylistWithMusic(playlistId: Long): PlaylistWithMusic
}