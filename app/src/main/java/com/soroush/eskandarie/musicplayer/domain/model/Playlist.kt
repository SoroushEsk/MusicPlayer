package com.soroush.eskandarie.musicplayer.domain.model

import android.net.Uri

data class Playlist(
    val id : Int,
    val name : String,
    val poster : Uri
)
