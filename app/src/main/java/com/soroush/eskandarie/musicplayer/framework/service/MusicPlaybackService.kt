package com.soroush.eskandarie.musicplayer.framework.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.media3.session.MediaSession
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import com.google.common.util.concurrent.ListenableFuture
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.GetAllMusicOfQueueUseCase
import com.soroush.eskandarie.musicplayer.framework.notification.MusicNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@AndroidEntryPoint
@UnstableApi
class MusicPlaybackService: Service() {
    //region Fields
    @Inject lateinit var mediaSession: MediaSession
    @Inject lateinit var usecase: GetAllMusicOfQueueUseCase
    private lateinit var notificationManager: NotificationManager
    private lateinit var musicNotificationManager: MusicNotificationManager
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "MediaPlaybackChannel"
    }

    //endregion
    //region Lifecycle Methods
    override fun onCreate() {
        super.onCreate()
        pausePlayback()
        notificationManager = getSystemService(NotificationManager::class.java)
        musicNotificationManager = MusicNotificationManager(this)
        createNotificationChannel()

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        CoroutineScope(Dispatchers.IO).launch{
            usecase().forEach{
                if (it.id.toInt() == 2 ){
                    val mediaItems = usecase().map { music ->
                        MediaItem.fromUri(music.path)
                    }

                    withContext(Dispatchers.Main) {
                        playMusic(mediaItems)
                    }
                }
                Log.e("music paht", "id:${it.id} path:${it.path}")
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
    private fun pausePlayback(){
        Log.e("song playback service", "pause")
    }
    private fun resumePlayback(){
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
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        // You can use your MusicNotificationManager or create a basic notification here
        // For simplicity, I'm creating a basic notification
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.rec)
            .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession))
            .setContentTitle("Wonderful music")
            .setContentText("My Awesome Band")
            .build()
    }
    //endregion
}

