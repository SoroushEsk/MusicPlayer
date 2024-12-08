package com.soroush.eskandarie.musicplayer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.soroush.eskandarie.musicplayer.R

class MusicPlayerService : Service() {

    private var exoPlayer: ExoPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "MusicPlayerService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notificationLayout = RemoteViews(packageName, R.layout.music_notification)

        val customNotification = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setContentTitle("Music Player")
            .setContentText("Playing music")
            .build()

        // Start as a foreground service
        startForeground(666, customNotification)

        Log.d(TAG, "Service Started")

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
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "Service Destroyed")
        exoPlayer?.release()
        exoPlayer = null
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "Music Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for music player notifications"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
