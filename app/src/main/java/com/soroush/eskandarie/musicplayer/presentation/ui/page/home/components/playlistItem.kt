package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.presentation.ui.model.PlaylistDropdownItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    posterShape: Shape = RoundedCornerShape(Dimens.CornerRadius.General),
    icon1: Int = R.drawable.play_button,
    icon2: Int = R.drawable.shuffle,
    errorImage: Int = R.drawable.empty_album,
    themeColor: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    playlistId: Int,
    title: String,
    posterUri: Uri,
    onIcon1Click: () -> Unit,
    onIcon2Click: () -> Unit,
    dropdownList: List<PlaylistDropdownItem>,
    onClick: () -> Unit

) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val backgroundColr by remember{
        mutableStateOf(
            if ( themeColor is LightTheme ) Color.White.copy(alpha = 0.91f)
            else Color.Black.copy(alpha = 0.91f)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.Size.GeneralItemHeight)
            .clip(posterShape)
            .background(themeColor.DarkSurface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(Dimens.Padding.PlaylistItemPoster)
                .clip(posterShape),
            model = posterUri,
            contentScale = ContentScale.Crop,
            contentDescription = "Playlist: ${title} Poster",
            error = painterResource(id = errorImage)
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(Dimens.Size.PlaylistSpaceBetween)
        )
        Text(
            text = title,
            color = themeColor.Text,
            style = textStyle
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(Dimens.Size.PlayListItemIconSize)
                .padding(Dimens.Padding.PlaylistItemIcon),
            painter = painterResource(id = icon1),
            contentDescription = "PlayButton",
            tint = themeColor.Icon
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(Dimens.Size.PlaylistSpaceBetween)
        )
        Icon(
            modifier = Modifier.size(Dimens.Size.PlayListItemIconSize),
            painter = painterResource(id = icon2),
            contentDescription = "ShufflePlayButton",
            tint = themeColor.Icon
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(Dimens.Size.PlaylistSpaceBetween)
        )

        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = themeColor.Text

                )
            }
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(Dimens.CornerRadius.TopPlaylistItem))
            ) {
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier
                        .background(backgroundColr)
                        .padding(
                            Dimens.Padding.PlayListItemDropdown,
                            Dimens.Padding.PlayListItemDropdown / 2
                        ),
                    properties = PopupProperties(
                        focusable = true,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                ) {
                    dropdownList.forEachIndexed { index, value ->
                        DropdownMenuItem(
                            modifier = Modifier.background(Color.Transparent),
                            text = {
                                Text(
                                    text = value.name,
                                    color = themeColor.Text,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                value.onClick(playlistId)
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
