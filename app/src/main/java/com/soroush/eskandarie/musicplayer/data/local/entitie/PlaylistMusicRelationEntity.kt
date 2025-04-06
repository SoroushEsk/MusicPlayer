package com.soroush.eskandarie.musicplayer.data.local.entitie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.soroush.eskandarie.musicplayer.util.Constants

@Entity(
    tableName = Constants.Database.MusicPlaylistRelationTableName,
    primaryKeys = [
        Constants.Database.MusicPlaylistPlaylistIdColumn,
        Constants.Database.MusicPlaylistMusicIdColumn
    ],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = [Constants.Database.PlaylistIdColumn],
            childColumns = [Constants.Database.MusicPlaylistPlaylistIdColumn],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = [Constants.Database.MusicIdColumn],
            childColumns = [Constants.Database.MusicPlaylistMusicIdColumn],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaylistMusicRelationEntity(
    @ColumnInfo(name = Constants.Database.MusicPlaylistPlaylistIdColumn)
    val playlistId: Long,
    @ColumnInfo(name = Constants.Database.MusicPlaylistMusicIdColumn)
    val musicId: Long
)
