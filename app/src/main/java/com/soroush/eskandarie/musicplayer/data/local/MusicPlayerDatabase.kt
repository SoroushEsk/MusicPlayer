package com.soroush.eskandarie.musicplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao

@Database(entities = [MusicEntity::class], version = 1, exportSchema = false)
abstract class MusicPlayerDatabase :RoomDatabase(){
    abstract fun getMusicQueueDao(): MusicQueueDao
}