package com.soroush.eskandarie.musicplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao
import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistDao
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicQueueEntity

@Database(entities = [MusicQueueEntity::class], version = 1, exportSchema = false)
abstract class MusicPlayerDatabase :RoomDatabase(){
    abstract fun getMusicQueueDao(): MusicQueueDao
    abstract fun getPlaylistDao(): PlaylistDao
}