package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1

@Composable
fun TopPlaylistItem(
    title: String,
    uriFront: Uri,
    uriBack1: Uri,
    uriBack2: Uri,
    modifier: Modifier = Modifier,
    themeColor: ColorTheme = if(isSystemInDarkTheme()) DarkTheme else LightTheme,
    extraPadding: Dp = Dimens.Padding.TopPlaylistItemDefaultPadding,
    imageShape: Shape = RoundedCornerShape(Dimens.CornerRadius.TopPlaylistItemCornerRadius),
    errorResource: Int = R.drawable.empty_album,
    textColor: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.Medium
    )
) {
    var frontImageSize by remember { mutableStateOf(Size.Zero) }
    var mainBoxSize by remember { mutableStateOf(Size.Zero) }
    var prevBoxSize by remember { mutableStateOf(Size.Zero) }
    var padding by remember { mutableStateOf(0.dp) }
    LaunchedEffect1(frontImageSize, mainBoxSize) {
        if (frontImageSize != Size.Zero && Math.abs(prevBoxSize.width - mainBoxSize.width) > 100) {
            // finding out the minimum padding necessary to fit the all three image in
            val M =
                (Math.sin(Dimens.Rotation.TopPlaylistItemBackRotation * Math.PI / 180) * Dimens.Size.TopPlaylistItemBackWeight + 2f / 3)
            val minSide = Math
                .min(mainBoxSize.width, mainBoxSize.height)
            padding = ((minSide / 4) * (1 - M) / M).dp
            prevBoxSize = mainBoxSize
        }
    }
    val mainBoxPadding = (padding) / 1.5f
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .padding(extraPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1 - Dimens.Size.TopPlaylistItemTextWeight)
                .aspectRatio(1f)
                .padding(mainBoxPadding, 0.dp)
                .onGloballyPositioned { coordinates ->
                    val width = coordinates.size.width
                    val height = coordinates.size.height
                    mainBoxSize = Size(
                        width.toFloat(),
                        height.toFloat()
                    )
                },
            contentAlignment = Alignment.TopCenter
        ) {
            BackPosters(
                shape = imageShape,
                fillSize = Dimens.Size.TopPlaylistItemBackWeight,
                frontImageSize = frontImageSize,
                direction = Direction.Right,
                uri = uriBack2,
                description = "Playlist back poster image right",
                erroResource = errorResource
            )
            BackPosters(
                shape = imageShape,
                fillSize = Dimens.Size.TopPlaylistItemBackWeight,
                frontImageSize = frontImageSize,
                direction = Direction.Left,
                uri = uriBack1,
                description = "Playlist back poster image left",
                erroResource = errorResource
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(imageShape)
                    .aspectRatio(1f)
                    .onGloballyPositioned { coordinates ->
                        val width = coordinates.size.width
                        val height = coordinates.size.height
                        frontImageSize = Size(width.toFloat(), height.toFloat())
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                AsyncImage(
                    model = uriFront,
                    contentDescription = "Playlist poster front image",
                    modifier = Modifier
                        .fillMaxSize(),
                    error = painterResource(errorResource),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(Dimens.Size.TopPlaylistItemTextWeight),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = title,
                modifier = Modifier,
                style = textColor,
                color = themeColor.Text
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
    }

}

@Composable
private fun BackPosters(
    shape: Shape,
    fillSize: Float,
    frontImageSize: Size,
    direction: Direction,
    uri: Uri,
    description: String? = null,
    erroResource: Int

) {
    val sign = if (direction == Direction.Right) 1 else -1
    Box(
        modifier = Modifier
            .fillMaxSize(fillSize)
            .aspectRatio(1f)
            .rotate(sign * Dimens.Rotation.TopPlaylistItemBackRotation)
            .offset {
                IntOffset(
                    sign * (frontImageSize.width / 3).toInt(),
                    (frontImageSize.height / 8).toInt()
                )
            }
            .clip(shape),
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            model = uri,
            contentDescription = description,
            modifier = Modifier.fillMaxSize(),
            error = painterResource(erroResource),
            contentScale = ContentScale.Fit
        )
    }
}

enum class Direction {
    Right, Left
}