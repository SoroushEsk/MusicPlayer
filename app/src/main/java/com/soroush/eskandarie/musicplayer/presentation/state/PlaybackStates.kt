package com.soroush.eskandarie.musicplayer.presentation.state

import android.graphics.Bitmap
import android.net.Uri
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile

data class PlaybackStates(
    val isFavorite: Boolean = false,
    val isPlaying : Boolean = false,
    val isShuffle : Boolean = false,
    val currentDuration: Long = 0,
    val totalDuration  : Long = 0,
    val musicPercent: Float = 0.0f,
    val artist          : String = "",
    val title           : String = "",
    val bitmapBitmap: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
    val repeatMode: RepeatMode = RepeatMode.No_Repeat

)
