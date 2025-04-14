package com.soroush.eskandarie.musicplayer.domain.model

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever

data class MusicFile(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String = "",
    val duration: Long,
    val recordingDate: String? = null,
    val genre: String? = null,
    val size: Long = 0,
    val path: String
)
fun MusicFile.getAlbumArtBitmap(path: String): Bitmap? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        art?.let {
            android.graphics.BitmapFactory.decodeByteArray(art, 0, art.size)
        }
    } catch (e: Exception) {
        null
    } finally {
        retriever.release()
    }
}
fun MusicFile. getAlbumArtBitmap(): Bitmap? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        art?.let {
            android.graphics.BitmapFactory.decodeByteArray(art, 0, art.size)
        }
    } catch (e: Exception) {
        null
    } finally {
        retriever.release()
    }
}