package com.soroush.eskandarie.musicplayer.presentation.ui.model

import android.net.Uri

data class PlaylistDropdownItem(
    val id:     Int,
    val name:   String,
    val onClick: (id: Int) -> Unit
)