package com.soroush.eskandarie.musicplayer.data.local.entitie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soroush.eskandarie.musicplayer.util.Constants

@Entity(tableName = Constants.Database.MusicTableName)
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.Database.MusicIdColumn)
    val id: Long,
    @ColumnInfo(name = Constants.Database.MusicTitleColumn)
    val title: String,
    @ColumnInfo(name = Constants.Database.MusicArtistColumn)
    val artist: String,
    @ColumnInfo(name = Constants.Database.MusicPosterPathColumn)
    val posterPath: String,
    @ColumnInfo(name = Constants.Database.MusicIsFavoriteColumn)
    val isFavorite: Boolean,
    @ColumnInfo(name = Constants.Database.MusicPathColumn)
    val path: String,
    @ColumnInfo(name = Constants.Database.MusicPlayCountColumn)
    val playCount: Long,
    @ColumnInfo(name = Constants.Database.MusicDurationColumn)
    val duration: Long,
    @ColumnInfo(name = Constants.Database.MusicDatePlayedColumn)
    val datePlayed: Long
)
