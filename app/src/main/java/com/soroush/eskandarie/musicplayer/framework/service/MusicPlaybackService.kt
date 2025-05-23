package com.soroush.eskandarie.musicplayer.framework.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.media3.session.MediaSession
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import androidx.media3.session.legacy.MediaMetadataCompat
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetAllMusicOfQueueUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetMusicFromQueueUseCase
import com.soroush.eskandarie.musicplayer.framework.notification.MusicNotificationManager
import com.soroush.eskandarie.musicplayer.presentation.ui.page.lockscreen.LockScreenActivity
import com.soroush.eskandarie.musicplayer.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

import javax.inject.Inject

@AndroidEntryPoint
@UnstableApi
class MusicPlaybackService : MediaSessionService() {
    //region Fields
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var getMusicByIdUseCase: GetMusicFromQueueUseCase
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var getAllMusicOfQueueUseCase: GetAllMusicOfQueueUseCase
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isFavorite = false
    private var isShuffle  = false

    private lateinit var notificationManager: PlayerNotificationManager
    private lateinit var musicNotificationManager: MusicNotificationManager
    companion object {
        private const val NOTIFICATION_ID = 2
        private const val CHANNEL_ID = "MediaPlaybackChannel"
        const val ACTION_FAVORITES = "custom_action_favorites"
        const val ACTION_SHUFFLE   = "custom_action_shuffle"
    }
    private val customCommandFavorites = SessionCommand(ACTION_FAVORITES, Bundle.EMPTY)
    private val customCommandShuffle   = SessionCommand(ACTION_SHUFFLE  , Bundle.EMPTY)
    //endregion
    //region Lifecycle Methods
    override fun onCreate() {
        super.onCreate()
        mediaSession = provideMediaSession(this)
        updateCustomLayout()
        getsong()
    }
    fun provideMediaSession(@ApplicationContext context: Context): MediaSession {
        val exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.addListener(
            object : Player.Listener{
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    sharedPreferences.edit().putString(
                        Constants.SharedPreference.CurrentSongId,
                        mediaItem?.mediaId.toString()
                    ).apply()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    sharedPreferences.edit().putLong(
                        Constants.SharedPreference.PlaylingSongDuration,
                        exoPlayer.currentPosition
                    ).apply()
                }
            }
        )
        isShuffle = exoPlayer.shuffleModeEnabled
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)
        val mediaSession = MediaSession.Builder(context, exoPlayer)
            .setCallback(object: MediaSession.Callback  {
                override fun onConnect(
                    session: MediaSession,
                    controller: MediaSession.ControllerInfo
                ): MediaSession.ConnectionResult {
                    return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                        .setAvailableSessionCommands(
                            MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                                .add(customCommandFavorites)
                                .add(customCommandShuffle)
                                .build()
                        )
                        .build()
                }
                override fun onCustomCommand(
                    session: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    customCommand: SessionCommand,
                    args: Bundle
                ): ListenableFuture<SessionResult> {
                    if (customCommand.customAction == ACTION_FAVORITES) {
//                saveToFavorites(session.player.currentMediaItem)
                        isFavorite =!isFavorite
                        updateCustomLayout()
                        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
                    }
                    if (customCommand.customAction == ACTION_SHUFFLE) {
                        isShuffle = !isShuffle
                        exoPlayer.shuffleModeEnabled = isShuffle
                        updateCustomLayout()
                        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
                    }
                    return super.onCustomCommand(session, controller, customCommand, args)
                }
            })
            .build()
        return mediaSession
    }
    private fun updateCustomLayout() {
        val favoriteButton =
            CommandButton.Builder(if(isFavorite) CommandButton.ICON_HEART_FILLED else CommandButton.ICON_HEART_UNFILLED)
                .setDisplayName("Save to favorites")
                .setSessionCommand(customCommandFavorites)
                .build()

        val shuffleButton =
            CommandButton.Builder(if(isShuffle)CommandButton.ICON_SHUFFLE_ON else CommandButton.ICON_SHUFFLE_OFF)
                .setDisplayName("Shuffle")
                .setSessionCommand(customCommandShuffle)
                .build()

        mediaSession.setCustomLayout(ImmutableList.of(favoriteButton, shuffleButton))

        mediaSession.setMediaButtonPreferences(ImmutableList.of(favoriteButton, shuffleButton))
    }

    private fun getsong() {
        serviceScope.launch(Dispatchers.IO) {
            val mutableList = mutableListOf<MediaItem>()
            val music = getAllMusicOfQueueUseCase().forEach {
                val mediaItem = MediaItem.Builder()
                    .setMediaId(it.id.toString())
                    .setUri(it.path)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(MusicFile.getMusicTitle(it.path))
                            .setArtist(MusicFile.getMusicArtist(it.path))
                            .setDescription(it.id.toString())
                            .setArtworkUri(getArtworkUri(this@MusicPlaybackService, it.path))
                            .build()
                    )
                    .build()
                mutableList.add(mediaItem)
            }
            withContext(Dispatchers.Main) {
                val id: String =
                    sharedPreferences.getString(Constants.SharedPreference.CurrentSongId, "").toString()
                val duration: Long = sharedPreferences.getLong(Constants.SharedPreference.PlaylingSongDuration, 0)
                mediaSession.player.addMediaItems(mutableList)
                val mediaItemIndex = mutableList.indexOfFirst {
                    it.mediaId == id
                }
                if(mediaItemIndex != -1){
                    mediaSession.player.seekTo(mediaItemIndex, duration)
                }
                mediaSession.player.prepare()
            }
        }
    }
    override fun onDestroy() {
        with(sharedPreferences.edit()){
            putLong(Constants.SharedPreference.PlaylingSongDuration, mediaSession.player.currentPosition)
            putString(Constants.SharedPreference.CurrentSongId, mediaSession.player.currentMediaItem?.mediaId.toString())
            apply()
        }
        mediaSession.run {
            player.release()
            release()
        }

        super.onDestroy()
    }
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    //endregionno //
    //region Notification
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Music playback controls"
            }
        }
    }

    private fun createNotification(): Notification {
        val compatToken = mediaSession.sessionCompatToken

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(
                mediaSession.player.currentMediaItem?.mediaMetadata?.title ?: "Unknown Title"
            )
            .setContentText(
                mediaSession.player.currentMediaItem?.mediaMetadata?.artist ?: "Unknown Artist"
            )
            .setLargeIcon(
                MusicFile.getAlbumArtBitmap(
                    mediaSession.player.currentMediaItem?.mediaMetadata?.artworkUri?.toString()
                        ?: "",
                    this
                )
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(compatToken) // 🔥 This enables lock screen!
                    .setShowActionsInCompactView(0, 1, 2) // play, pause, etc.
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }
    private fun getArtworkUri(context: Context, audioPath: String): Uri? {
        try {
            val bitmap = MusicFile.getAlbumArtBitmap(audioPath, context)
            // Check if bitmap is valid
            if (bitmap != null && !bitmap.isRecycled && bitmap.width > 0) {
                val file = File(context.cacheDir, "cover_${audioPath.hashCode()}.jpg")
                FileOutputStream(file).use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                }
                return FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            }
        } catch (e: Exception) {
            Log.e("MusicPlaybackService", "Error saving artwork: ${e.message}")
        }
        // Return a default artwork URI or null
        return null
    }

    //endregion

}


