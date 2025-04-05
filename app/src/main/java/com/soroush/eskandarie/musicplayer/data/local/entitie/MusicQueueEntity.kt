package com.soroush.eskandarie.musicplayer.data.local.entitie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soroush.eskandarie.musicplayer.util.Constants

@Entity(tableName = Constants.Database.MusicQueueTableName)
data class MusicQueueEntity(
    @PrimaryKey(/*autoGenerate = true*/)
    @ColumnInfo(name = Constants.Database.MusicQueueIdColumn)
    val id:   Long,
    @ColumnInfo(name = Constants.Database.MusicQueuePathColumn)
    val path: String,
    @ColumnInfo(name = Constants.Database.MusicQueueIsFavoriteColumn)
    val isFavorite: Boolean
)
