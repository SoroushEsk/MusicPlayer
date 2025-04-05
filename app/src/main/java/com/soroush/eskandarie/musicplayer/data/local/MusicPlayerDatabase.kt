package com.soroush.eskandarie.musicplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicDao
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao
import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistDao
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity
import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistEntity

@Database(entities = [MusicQueueEntity::class, PlaylistEntity::class, MusicEntity::class], version = 1, exportSchema = false)
abstract class MusicPlayerDatabase :RoomDatabase(){
    abstract fun getMusicQueueDao(): MusicQueueDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getMusicDao(): MusicDao
}