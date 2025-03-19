package com.soroush.eskandarie.musicplayer.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.room.Room
import com.soroush.eskandarie.musicplayer.data.local.MusicPlayerDatabase
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao
import com.soroush.eskandarie.musicplayer.data.repository.MusicQueueRepositoryImpl
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetAllMusicOfQueueUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.RefreshQueueUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule{

    @Provides
    @Singleton
    fun provideMediaSessin(@ApplicationContext context: Context): MediaSession {
        val exoPlayer = ExoPlayer.Builder(context).build()
        val mediaSession = androidx.media3.session.MediaSession.Builder(context, exoPlayer)
            .setCallback(object: androidx.media3.session.MediaSession.Callback{

            })
            .build()
        return mediaSession
    }

    @Provides
    @Singleton
    fun provideMusicDatabase(@ApplicationContext context: Context): MusicPlayerDatabase{
        return Room.databaseBuilder(
            context,
            MusicPlayerDatabase::class.java,
            "music_player_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMusicQueueDao(musicPlayerDatabase: MusicPlayerDatabase) = musicPlayerDatabase.getMusicQueueDao()

    @Provides
    @Singleton
    fun provideMusicQueueRepository(musicQueueDao: MusicQueueDao): MusicQueueRepository {
        return MusicQueueRepositoryImpl(musicQueueDao)
    }

    @Provides
    @Singleton
    fun provideGetAllMusicFromQueueUseCase(musicQueueRepository: MusicQueueRepository): GetAllMusicOfQueueUseCase {
        return GetAllMusicOfQueueUseCase(musicQueueRepository)
    }

    @Provides
    @Singleton
    fun provideRefreshQueueUseCase(musicQueueRepository: MusicQueueRepository): RefreshQueueUseCase {
        return RefreshQueueUseCase(musicQueueRepository)
    }

}