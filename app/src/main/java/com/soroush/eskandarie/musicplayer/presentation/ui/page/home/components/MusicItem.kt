package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.getAlbumArtBitmap
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme

@Preview(showBackground = true)
@Composable
fun PlaylistPage(){
    MusicItem(Modifier,
        MusicFile(
            id = 12,
            title = "Z - The warning",
            artist = "The warning",
            album = "",
            duration = 213443,
            recordingDate = null,
            genre = null,
            size = 234,
            path = "/storage/emulated/0/Download/NeginKt - Paiz   Saraabe Toe.mp3"
        ),
        isPlaying = false)
}
@Composable
fun MusicItem(
    modifier: Modifier = Modifier,
    music: MusicFile,
    colorTheme: ColorTheme = if(isSystemInDarkTheme()) DarkTheme else LightTheme,
    pauseIconId: Int = R.drawable.pause,
    resumeIconId: Int = R.drawable.play_button,
    defaultAlbumArt: Int = R.drawable.empty_album,
    isPlaying: Boolean,
) {
    val albumArtBitmap = music.getAlbumArtBitmap() ?: BitmapFactory.decodeResource(
        LocalContext.current.resources,
        defaultAlbumArt
    )
    Row (
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .fillMaxWidth()
            .height(92.dp)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(colorTheme.DarkShadow.copy(alpha = 0.4f), Color.Transparent, Color.Transparent)
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, colorTheme.LightShadow.copy(alpha = 0.3f))
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .background(colorTheme.Surface
            )
    ){
        SongAlbumArt(
            songAlbumArt = albumArtBitmap,
            pauseIconId = pauseIconId,
            resumeIconId = resumeIconId,
            isPlaying = isPlaying,
            playStatusIconColor = colorTheme.Text
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 16.dp)
        ) {
            Text(
                text = music.title,
                color = colorTheme.Text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                maxLines = 1
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = music.artist,
                    color = colorTheme.Text.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
                val minute = (music.duration / 1000) / 60
                val seconds = (music.duration / 1000) % 60
                val minuteString = if (minute >= 10) minute.toString() else "0$minute"
                val secondsString = if (seconds >= 10) seconds.toString() else "0$seconds"
                Text(
                    text = "$minuteString:$secondsString",
                    color = colorTheme.Text.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
            }
        }
    }

}

@Composable
fun SongAlbumArt(
    modifier: Modifier = Modifier,
    songAlbumArt: Bitmap,
    resumeIconId: Int,
    pauseIconId: Int,
    isPlaying: Boolean,
    playStatusIconColor: Color
) {
    var isResume by remember {
        mutableStateOf(true)
    }
    Box (
        modifier = modifier
            .padding(10.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(26.dp))
            .clickable {
                if (isResume) isResume = false
                else isResume = true
            },
        contentAlignment = Alignment.Center
    ){
        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = songAlbumArt.asImageBitmap(),
            contentDescription = "playlist_page_music_item_album_art_image"
        )
        if(isPlaying){
            Icon(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .alpha(0.8f),
                tint = playStatusIconColor,
                painter = painterResource(id = if(isResume) resumeIconId else pauseIconId),
                contentDescription = "playlist_page_music_item_play_status_image"
            )
        }
    }

}
