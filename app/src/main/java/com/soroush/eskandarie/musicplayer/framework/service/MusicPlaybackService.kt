package com.soroush.eskandarie.musicplayer.framework.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.media3.session.MediaSession
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import androidx.media3.session.legacy.MediaMetadataCompat
import androidx.media3.ui.PlayerNotificationManager
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetAllMusicOfQueueUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetMusicFromQueueUseCase
import com.soroush.eskandarie.musicplayer.framework.notification.MusicNotificationManager
import com.soroush.eskandarie.musicplayer.presentation.ui.page.lockscreen.LockScreenActivity
import dagger.hilt.android.AndroidEntryPoint
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
    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var getMusicByIdUseCase: GetMusicFromQueueUseCase

    @Inject
    lateinit var getAllMusicOfQueueUseCase: GetAllMusicOfQueueUseCase
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private lateinit var notificationManager: PlayerNotificationManager
    private lateinit var musicNotificationManager: MusicNotificationManager
    companion object {
        private const val NOTIFICATION_ID = 2
        private const val CHANNEL_ID = "MediaPlaybackChannel"
        const val COMMAND_REPEAT = "custom_repeat"
        const val COMMAND_SHUFFLE = "custom_shuffle"
        const val COMMAND_LIKE = "custom_like"
    }

    //endregion
    //region Lifecycle Methods
    override fun onCreate() {
        super.onCreate()
        setMediaNotificationProvider(CustomMediaNotificationProvider())
//        getsong()
    }
    private fun getsong(): Int {
        serviceScope.launch {
            val mutableList = mutableListOf<MediaItem>()
            val music = getAllMusicOfQueueUseCase().forEach {
                val mediaItem = MediaItem.Builder()
                    .setMediaId(it.id.toString())
                    .setUri(it.path)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(MusicFile.getMusicTitle(it.path))
                            .setDescription("${it.id}")
                            .setArtist(MusicFile.getMusicArtist(it.path))
                            .setArtworkUri(getArtworkUri(this@MusicPlaybackService, it.path))
                            .build()
                    )
                    .build()
                mutableList.add(mediaItem)
            }
            withContext(Dispatchers.Main) {
                mediaSession.player.addMediaItems(mutableList)
                mediaSession.player.prepare()
            }
        }
        return START_STICKY
    }
    override fun onDestroy() {
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


