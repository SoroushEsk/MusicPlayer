package com.soroush.eskandarie.musicplayer.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_queue")
data class MusicEntity(
    @PrimaryKey(/*autoGenerate = true*/)
    @ColumnInfo(name = "id")
    val id:   Long,
    @ColumnInfo(name = "music_path")
    val path: String
)
