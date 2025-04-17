package com.soroush.eskandarie.musicplayer.framework.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.session.MediaSession
import android.os.Build
import androidx.core.app.NotificationCompat
import com.soroush.eskandarie.musicplayer.R

class MusicNotificationManager(private val context: Context) {


}
//        notificationManager = PlayerNotificationManager.Builder(
//            this,
//            NOTIFICATION_ID,
//            CHANNEL_ID
//        )
//            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
//                override fun getCurrentContentTitle(player: Player): CharSequence {
//                    return player.mediaMetadata.title ?: "Unknown Title"
//                }
//
//                override fun createCurrentContentIntent(player: Player): PendingIntent? {
//                    return null
//                }
//
//                override fun getCurrentContentText(player: Player): CharSequence? {
//                    return player.mediaMetadata.artist
//                }
//
//                override fun getCurrentLargeIcon(
//                    player: Player,
//                    callback: PlayerNotificationManager.BitmapCallback
//                ): Bitmap? {
//                    val artworkUri = player.mediaMetadata.artworkUri
//                    if (artworkUri != null) {
//                        val bitmap = MusicFile.getAlbumArtBitmap(
//                            artworkUri.toString(),
//                            this@MusicPlaybackService
//                        )
//                        callback.onBitmap(bitmap)
//                        return bitmap
//                    }
//                    return null
//                }
//            })
//            .setChannelImportance(NotificationManager.IMPORTANCE_LOW)
//            .setSmallIconResourceId(R.mipmap.ic_launcher)
//            .build()
//        notificationManager.setUseRewindAction(true)
//        notificationManager.setUseFastForwardAction(true)
//        notificationManager.setMediaSessionToken(mediaSession.sessionCompatToken)
//        notificationManager.setPlayer(mediaSession.player)
////        musicNotificationManager = MusicNotificationManager(this)
////        createNotificationChannel()
//        val notification = createNotification()
//        startForeground(NOTIFICATION_ID, notification)