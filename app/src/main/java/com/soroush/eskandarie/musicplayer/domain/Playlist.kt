package com.soroush.eskandarie.musicplayer.domain

import android.net.Uri

data class Playlist(
    val id : Int,
    val name : String,
    val poster : Uri
)
