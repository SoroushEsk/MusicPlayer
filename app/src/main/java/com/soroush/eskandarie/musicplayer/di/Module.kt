package com.soroush.eskandarie.musicplayer.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.room.Room
import com.soroush.eskandarie.musicplayer.data.local.MusicPlayerDatabase
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicDao
import com.soroush.eskandarie.musicplayer.data.local.dao.MusicQueueDao
import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistDao
import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistMusicRelationDao
import com.soroush.eskandarie.musicplayer.data.local.dao.PlaylistWithMusicDao
import com.soroush.eskandarie.musicplayer.data.repository.DeviceMusicRepositoryImpl
import com.soroush.eskandarie.musicplayer.data.repository.MusicPlaylistRelationRepositoryImp
import com.soroush.eskandarie.musicplayer.data.repository.MusicQueueRepositoryImpl
import com.soroush.eskandarie.musicplayer.data.repository.MusicRepositoryImp
import com.soroush.eskandarie.musicplayer.data.repository.PlaylistRepositoryImp
import com.soroush.eskandarie.musicplayer.data.repository.PlaylistWithMusicRepositoryImp
import com.soroush.eskandarie.musicplayer.domain.repository.DeviceMusicRepository
import com.soroush.eskandarie.musicplayer.domain.repository.MusicPlaylistRelationRepository
import com.soroush.eskandarie.musicplayer.domain.repository.MusicQueueRepository
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import com.soroush.eskandarie.musicplayer.domain.repository.PlaylistRepository
import com.soroush.eskandarie.musicplayer.domain.repository.PlaylistWithMusicRepository
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDeviceUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.AnalyzeFoldersUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.Get100MostPlayedMusicsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.Get100RecentlyPlayedMusicListUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetFavoriteMusicFilesUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music.GetPlaylistWithAllMusicFileByIdUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetMusicFileByIdFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.GetTracksWithUsualOrderLimitedUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.music.ModifyMusicStatusUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.CreateANewPlaylistUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.DeleteAPlaylistUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.GetAllPlaylistItemsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.GetNumberOfPlaylistsUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.ModifyAPlaylistUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music.AddAMusicToPlaylistUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist_music.AddListOfMusicToAPlaylistUseCase
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
    //region SharedPreference
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context) = context.applicationContext.getSharedPreferences(
        Constants.SharedPreference.Name,
        Context.MODE_PRIVATE
    )!!
    //endregion
    //region MediaSession
    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideMediaSession(@ApplicationContext context: Context): MediaSession {
        val exoPlayer = ExoPlayer.Builder(context).build()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)
        val mediaSession = MediaSession.Builder(context, exoPlayer)
            .setCallback(object: MediaSession.Callback  {
            })
            .build()
        return mediaSession
    }

    //endregion
    //region Database + Dao
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
    @Provides
    @Singleton
    fun providePlaylistRepository(playlistDao: PlaylistDao): PlaylistRepository =
        PlaylistRepositoryImp(playlistDao)
    @Provides
    @Singleton
    fun providePlaylistWithMusicRepository(playlistWithMusicDao: PlaylistWithMusicDao): PlaylistWithMusicRepository =
        PlaylistWithMusicRepositoryImp(playlistWithMusicDao)
    @Provides
    @Singleton
    fun provideMusicPlaylistRelationRepository(playlistMusicRelationDao: PlaylistMusicRelationDao): MusicPlaylistRelationRepository =
        MusicPlaylistRelationRepositoryImp(playlistMusicRelationDao)
    //endregion
    //region Music Queue Use Cases
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
    //region Content Resolver Use Case
    //endregion
    //region Playlist Use Cases
    @Provides
    @Singleton
    fun provideModifyAPlaylistUseCase(playlistRepository: PlaylistRepository): ModifyAPlaylistUseCase =
        ModifyAPlaylistUseCase(playlistRepository)
    @Provides
    @Singleton
    fun provideGetAllPlaylistItemsUseCase(playlistRepository: PlaylistRepository): GetAllPlaylistItemsUseCase =
        GetAllPlaylistItemsUseCase(playlistRepository)
    @Provides
    @Singleton
    fun provideCreateANewPlaylistUseCase(playlistRepository: PlaylistRepository): CreateANewPlaylistUseCase =
        CreateANewPlaylistUseCase(playlistRepository)
    @Provides
    @Singleton
    fun provideDeleteAPlaylistUseCase(playlistRepository: PlaylistRepository): DeleteAPlaylistUseCase =
        DeleteAPlaylistUseCase(playlistRepository)
    @Provides
    @Singleton
    fun provideGetNumberOfPlaylistsUseCase(playlistRepository: PlaylistRepository): GetNumberOfPlaylistsUseCase =
        GetNumberOfPlaylistsUseCase(playlistRepository)
    //endregion
    //region Music Use Cases
    @Provides
    @Singleton
    fun provideModifyMusicStatusUseCase(musicRepository: MusicRepository): ModifyMusicStatusUseCase =
        ModifyMusicStatusUseCase(musicRepository)
    @Provides
    @Singleton
    fun provideGetAllMusicFilesFromDatabase(musicRepository: MusicRepository): GetAllMusicFromDatabaseUseCase{
        return GetAllMusicFromDatabaseUseCase(musicRepository)
    }
    @Provides
    @Singleton
    fun provideGetMusicFileByIdFromDatabaseUseCase(musicRepository: MusicRepository): GetMusicFileByIdFromDatabaseUseCase{
        return GetMusicFileByIdFromDatabaseUseCase(musicRepository)
    }
    @Provides
    @Singleton
    fun provideGet100MostPlayedMusicsUseCase(musicRepository: MusicRepository): Get100MostPlayedMusicsUseCase =
        Get100MostPlayedMusicsUseCase(musicRepository)
    @Provides
    @Singleton
    fun provideGet100RecentlyPlayedMusicListUseCase(musicRepository: MusicRepository): Get100RecentlyPlayedMusicListUseCase =
        Get100RecentlyPlayedMusicListUseCase(musicRepository)
    @Provides
    @Singleton
    fun provideGetTracksWithUsualOrderLimitedUseCase(musicRepository: MusicRepository): GetTracksWithUsualOrderLimitedUseCase =
        GetTracksWithUsualOrderLimitedUseCase(musicRepository)
    @Provides
    @Singleton
    fun provideGetFavoriteMusicFilesUseCase(musicRepository: MusicRepository): GetFavoriteMusicFilesUseCase =
        GetFavoriteMusicFilesUseCase(musicRepository)
    @Provides
    @Singleton
    fun provideAnalyzeFoldersUseCase(musicRepository: MusicRepository): AnalyzeFoldersUseCase =
        AnalyzeFoldersUseCase(musicRepository)
    //endregion
    //region PlayList-Music UseCase
    @Provides
    @Singleton
    fun provideGetPlaylistWithAllMusicFileByIdUseCase(playlistWithMusicRepository: PlaylistWithMusicRepository): GetPlaylistWithAllMusicFileByIdUseCase =
        GetPlaylistWithAllMusicFileByIdUseCase(playlistWithMusicRepository)
    @Provides
    @Singleton
    fun proviceAddListOfMusicToAPlaylistUseCase(musicPlaylistRelationRepository: MusicPlaylistRelationRepository): AddListOfMusicToAPlaylistUseCase =
        AddListOfMusicToAPlaylistUseCase(musicPlaylistRelationRepository)
    @Provides
    @Singleton
    fun provideAddAMusicToPlaylistUseCase(musicPlaylistRelationRepository: MusicPlaylistRelationRepository): AddAMusicToPlaylistUseCase =
        AddAMusicToPlaylistUseCase(musicPlaylistRelationRepository)
    //endregion
}