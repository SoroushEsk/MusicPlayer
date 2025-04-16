package com.soroush.eskandarie.musicplayer.framework.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.media3.session.MediaSession
import android.util.Log
import androidx.core.app.NotificationCompat
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

import javax.inject.Inject

@AndroidEntryPoint
@UnstableApi
class MusicPlaybackService : Service() {
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
    }

    //endregion
    //region Lifecycle Methods
    override fun onCreate() {
        super.onCreate()

        notificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            CHANNEL_ID
        )
            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): CharSequence {
                    return player.mediaMetadata.title ?: "Unknown Title"
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    return player.mediaMetadata.artist
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    val artworkUri = player.mediaMetadata.artworkUri
                    if (artworkUri != null) {
                        val bitmap = MusicFile.getAlbumArtBitmap(
                            artworkUri.toString(),
                            this@MusicPlaybackService
                        )
                        callback.onBitmap(bitmap)
                        return bitmap
                    }
                    return null
                }
            })
            .setChannelImportance(NotificationManager.IMPORTANCE_LOW)
            .setSmallIconResourceId(R.mipmap.ic_launcher)
            .build()
        notificationManager.setUseRewindAction(true)
        notificationManager.setUseFastForwardAction(true)
        notificationManager.setMediaSessionToken(mediaSession.sessionCompatToken)
        notificationManager.setPlayer(mediaSession.player)
//        musicNotificationManager = MusicNotificationManager(this)
//        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        serviceScope.launch {
            val mutableList = mutableListOf<MediaItem>()
            val music = getAllMusicOfQueueUseCase().forEach {
//                val mediaItem = it.path.let { MediaItem.fromUri(it) }
                val mediaItem = MediaItem.Builder()
                    .setMediaId(it.id.toString())
                    .setUri(it.path)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(MusicFile.getMusicTitle(it.path))
                            .setArtist(MusicFile.getMusicArtist(it.path))
                            .setArtworkUri(Uri.parse(it.path))
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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //endregion
    //region Init Function
    //endregion
    //region Player Control
    private fun pausePlayback() {
        Log.e("song playback service", "pause")
    }

    private fun resumePlayback() {
        Log.e("song playback service", "resume")
    }

    private fun playMusic(mediaItems: List<MediaItem>) {
        val player = mediaSession.player // Get ExoPlayer from MediaSession
        player.setMediaItems(mediaItems)
        player.prepare()
        player.play()
    }

    //endregion
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

    //endregion
}

