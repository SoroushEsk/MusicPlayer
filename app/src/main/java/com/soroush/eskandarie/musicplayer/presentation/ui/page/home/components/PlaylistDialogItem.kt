package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.model.getAlbumArtBitmap
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.util.Constants

@Composable
fun PlaylistDialogItem(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    onClick: () -> Unit,
    colorTheme: ColorTheme,
    shape: Shape = RoundedCornerShape(Dimens.CornerRadius.General)

) {
    val albumArtBitmap = MusicFile.getAlbumArtBitmap(
        context = LocalContext.current,
        path = playlist.poster
    )
    val palette = Palette.from(albumArtBitmap).generate()
    val dominantColor1 = Color(palette.getDominantColor(0)).copy(alpha = 0.12f)
    val vibrantColor1 = Color(palette.getVibrantColor(0)).copy(alpha = 0.07f)
    val mutedColor = Color(palette.getMutedColor(0)).copy(alpha = 0.05f)
    val listOfColors =
        listOf(dominantColor1, vibrantColor1, mutedColor)
    val radialGradientBrush = Brush.horizontalGradient(
        colors = listOfColors
    )

    Row(
        modifier = modifier
            .clip(shape)
            .fillMaxWidth()
            .height(Dimens.Size.PlaylistDialogItemHeight)
            .border(
                width = Dimens.Size.MusicItemShadowBorder,
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorTheme.DarkShadow.copy(alpha = Dimens.Alpha.MusicItemDarkShadowBorderColor),
                        Color.Transparent,
                        Color.Transparent
                    )
                ),
                shape = shape
            )
            .border(
                width = Dimens.Size.MusicItemShadowBorder,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        colorTheme.LightShadow.copy(alpha = Dimens.Alpha.MusicItemLightShadowBorderColor)
                    )
                ),
                shape = shape
            )
            .background(colorTheme.Surface)
            .background(radialGradientBrush)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically

    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .padding(10.dp)
                .clip(shape),
            bitmap = albumArtBitmap.asImageBitmap(),
            contentDescription = Constants.MusicItem.MusicAlbumArtImageDescription
        )

        Text(
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
            text = playlist.name,
            color = colorTheme.Text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            maxLines = Constants.MusicItem.MusicItemDefaultMaxLine,
            overflow = TextOverflow.Ellipsis
        )
    }
}