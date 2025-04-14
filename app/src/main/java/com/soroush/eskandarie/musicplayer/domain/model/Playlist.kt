package com.soroush.eskandarie.musicplayer.domain.model

import com.soroush.eskandarie.musicplayer.data.local.entitie.PlaylistEntity

data class Playlist(
    val id: Long,
    val name: String,
    val poster: String
){
    fun toPlaylistEntity(): PlaylistEntity=
        PlaylistEntity(
            id = id,
            name = name,
            posterPath =  poster,
        )
}
