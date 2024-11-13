package com.soroush.eskandarie.musicplayer.presentation.ui.model

data class PlaylistDropdownItem(
    val id:     Int,
    val name:   String,
    val onClick: (id: Int) -> Unit
)