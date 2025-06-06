package com.soroush.eskandarie.musicplayer.data.local.entitie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.util.Constants

@Entity(tableName = Constants.Database.PlaylistTableName)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.Database.PlaylistIdColumn)
    val id: Long,
    @ColumnInfo(name = Constants.Database.PlaylistNameColumn)
    val name: String,
    @ColumnInfo(name = Constants.Database.PlaylistPosterPath)
    val posterPath: String
){
    fun toPlaylist(): Playlist{
        return Playlist(
            id = id,
            name = name,
            poster = posterPath
        )
    }
}
