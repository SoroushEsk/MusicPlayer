package com.soroush.eskandarie.musicplayer.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer


class MusicPlayerService : Service() {

    private var exoPlayer: ExoPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
//        TODO("Return the communication channel to the service.")
        return null
    }
    companion object {
        private const val TAG = "SimpleService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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

}