package com.soroush.eskandarie.musicplayer.domain.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.data.local.entitie.MusicEntity

data class MusicFile(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String = "",
    val duration: Long,
    val recordingDate: String? = null,
    val genre: String? = null,
    val size: Long = 0,
    val path: String,
    val isFavorite: Boolean,
    val playCount: Long,
    val datePlayed: Long
){
    companion object{

        fun getAlbumArtBitmap(path: String, context: Context): Bitmap {
            val retriever = MediaMetadataRetriever()
            return try {
                retriever.setDataSource(path)
                val art = retriever.embeddedPicture
                if (art != null) {
                    BitmapFactory.decodeByteArray(art, 0, art.size)
                } else {
                    BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
                }
            } catch (e: Exception) {
                BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
            } finally {
                retriever.release()
            }
        }
        fun getMusicTitle(filePath: String) : String{
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(filePath)

                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                return title.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            } finally {
                retriever.release()
            }
        }
        fun getMusicArtist(filePath: String): String {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(filePath)


                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                return artist.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            } finally {
                retriever.release()
            }
        }
    }
    fun toMusicEntity(): MusicEntity = MusicEntity(
        id = id,
        title = title,
        artist = artist,
        path = path,
        posterPath = "",
        isFavorite = isFavorite,
        playCount = playCount,
        duration = duration,
        datePlayed = datePlayed
    )
}
fun MusicFile.getAlbumArtBitmap(path: String = this.path): Bitmap? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        art?.let {
            BitmapFactory.decodeByteArray(art, 0, art.size)
        }
    } catch (e: Exception) {
        null
    } finally {
        retriever.release()
    }
}