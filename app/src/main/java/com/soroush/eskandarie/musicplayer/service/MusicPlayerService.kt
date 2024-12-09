package com.soroush.eskandarie.musicplayer.service
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.soroush.eskandarie.musicplayer.R

class MusicPlayerService : Service() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: NotificationManager

    companion object {
        private const val TAG = "MusicPlayerService"
        private const val CHANNEL_ID = "MUSIC_CHANNEL"
        private const val NOTIFICATION_ID = 666
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        initializeMediaSession()

        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(this).build()
            val audioUri = Uri.parse("android.resource://${packageName}/raw/a")
            val mediaItem = MediaItem.fromUri(audioUri)

            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                play()
            }
        }

        // Update the notification
        updateNotification()

        return START_STICKY
    }

    override fun onDestroy() {
        exoPlayer?.release()
        exoPlayer = null
        mediaSession.release()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for music player notifications"
            }
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initializeMediaSession() {
        mediaSession = MediaSessionCompat(this, TAG).apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    exoPlayer?.play()
                    updateNotification()
                }

                override fun onPause() {
                    exoPlayer?.pause()
                    updateNotification()
                }

                override fun onStop() {
                    stopSelf()
                }
            })

            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_STOP
                    )
                    .setState(
                        PlaybackStateCompat.STATE_PLAYING,
                        PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                        1.0f
                    )
                    .build()
            )

            setMetadata(
                MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Your Song Title")
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Your Artist Name")
                    .build()
            )

            isActive = true
        }
    }

    private fun updateNotification() {
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(0, 1) // Show Play/Pause in compact view

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Your Song Title")
            .setContentText("Your Artist Name")
            .setStyle(mediaStyle)
            .addAction(
                R.drawable.pause, "Pause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_PAUSE
                )
            )
            .addAction(
                R.drawable.pause, "Play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_PLAY
                )
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }
}
