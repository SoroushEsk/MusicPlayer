package com.soroush.eskandarie.musicplayer.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.room.Room
import com.soroush.eskandarie.musicplayer.data.local.MusicPlayerDatabase
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicDao
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao
import com.soroush.eskandarie.musicplayer.data.repository.DeviceMusicRepositoryImpl
import com.soroush.eskandarie.musicplayer.data.repository.MusicQueueRepositoryImpl
import com.soroush.eskandarie.musicplayer.data.repository.MusicRepositoryImp
import com.soroush.eskandarie.musicplayer.domain.repository.DeviceMusicRepository
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDeviceUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetAllMusicOfQueueUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetMusicFromQueueUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.RefreshQueueUseCase
import com.soroush.eskandarie.musicplayer.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule{
    //region ViewModels

    //endregion
    //region SharedPreference
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context) = context.getSharedPreferences(
        Constants.SharedPreference.Name,
        Context.MODE_PRIVATE
    )
    //endregion
    //region MediaSession
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
    //endregion
    //region Database
    @Provides
    @Singleton
    fun provideMusicDatabase(@ApplicationContext context: Context): MusicPlayerDatabase{
        return Room.databaseBuilder(
            context,
            MusicPlayerDatabase::class.java,
            Constants.Database.MusicPlayerDatabaseName
        ).build()
    }
    @Provides
    @Singleton
    fun provideMusicQueueDao(musicPlayerDatabase: MusicPlayerDatabase) = musicPlayerDatabase.getMusicQueueDao()
    @Provides
    @Singleton
    fun providePlaylistDao(musicPlayerDatabase: MusicPlayerDatabase) = musicPlayerDatabase.getPlaylistDao()
    @Provides
    @Singleton
    fun provideMusicDao(musicPlayerDatabase: MusicPlayerDatabase) = musicPlayerDatabase.getMusicDao()
    @Provides
    @Singleton
    fun providePlaylistMusicRelationDao(musicPlayerDatabase: MusicPlayerDatabase) = musicPlayerDatabase.getPlaylistMusicRelationDao()
    @Provides
    @Singleton
    fun providePlaylistWithMusicDao(musicPlayerDatabase: MusicPlayerDatabase) = musicPlayerDatabase.getPlaylistWithMusicDao()
    //endregion
    //region Repository
    @Provides
    @Singleton
    fun provideMusicQueueRepository(musicQueueDao: MusicQueueDao): MusicQueueRepository {
        return MusicQueueRepositoryImpl(musicQueueDao)
    }
    @Provides
    @Singleton
    fun provideDeviceMusicRepository(@ApplicationContext context: Context):DeviceMusicRepository{
        return DeviceMusicRepositoryImpl(context)
    }
    @Provides
    @Singleton
    fun provideMusicRepository(
        getAllMusicFromDeviceUseCase: GetAllMusicFromDeviceUseCase,
        musicDao: MusicDao
    ): MusicRepository = MusicRepositoryImp(
        getAllMusicFromDeviceUseCase,
        musicDao
    )
    //endregion
    //region MusicQueueUseCases
    @Provides
    @Singleton
    fun provideGetAllMusicFromQueueUseCase(musicQueueRepository: MusicQueueRepository): GetAllMusicOfQueueUseCase {
        return GetAllMusicOfQueueUseCase(musicQueueRepository)
    }
    @Provides
    @Singleton
    fun provideGetMusicFromQueueUseCase(musicQueueRepository: MusicQueueRepository): GetMusicFromQueueUseCase{
        return GetMusicFromQueueUseCase(musicQueueRepository)
    }
    @Provides
    @Singleton
    fun provideRefreshQueueUseCase(musicQueueRepository: MusicQueueRepository): RefreshQueueUseCase {
        return RefreshQueueUseCase(musicQueueRepository)
    }
    //endregion
}