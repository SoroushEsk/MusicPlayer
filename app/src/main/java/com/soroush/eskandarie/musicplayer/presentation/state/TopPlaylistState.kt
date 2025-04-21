package com.soroush.eskandarie.musicplayer.presentation.state

import android.graphics.Bitmap

data class TopPlaylistState(
    val name: String = "",
    val front: Bitmap? = null,
    val back_left: Bitmap? = null,
    val back_right: Bitmap? = null
)
