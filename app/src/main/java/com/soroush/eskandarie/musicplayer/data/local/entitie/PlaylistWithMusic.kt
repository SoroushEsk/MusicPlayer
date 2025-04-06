package com.soroush.eskandarie.musicplayer.data.local.entitie

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.soroush.eskandarie.musicplayer.util.Constants

data class PlaylistWithMusic(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumn = Constants.Database.PlaylistIdColumn,
        entityColumn = Constants.Database.MusicIdColumn,
        associateBy = Junction(
            PlaylistMusicRelationEntity::class,
            parentColumn = Constants.Database.MusicPlaylistPlaylistIdColumn,
            entityColumn = Constants.Database.MusicPlaylistMusicIdColumn
        )
    )
    val musicList: List<MusicEntity>
)
