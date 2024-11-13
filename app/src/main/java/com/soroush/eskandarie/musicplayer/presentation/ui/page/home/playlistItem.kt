package com.soroush.eskandarie.musicplayer.presentation.ui.page.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.presentation.ui.model.PlaylistDropdownItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dark
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Light
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ThemeColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    posterShape: Shape = RoundedCornerShape(Dimens.CornerRadius.PlaylistItemPosterRaduis),
    icon1: Int = R.drawable.play_button,
    icon2: Int = R.drawable.shuffle,
    errorImage: Int = R.drawable.empty_album,
    themeColor: ThemeColor = if (isSystemInDarkTheme()) Dark else Light,
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(themeColor.DarkSurface)
            .height(Dimens.Size.PlaylistItemHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(posterShape)
                .padding(Dimens.Padding.PlaylistItemIconPadding),
            model = posterUri,
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
            style = textStyle
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(Dimens.Size.PlayListItemIconSize)
                .padding(Dimens.Padding.PlaylistItemIconPadding),
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

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .background(themeColor.DarkSurface)
                    .padding(Dimens.Padding.PlayListItemDropdown)
            ) {
                dropdownList.forEachIndexed { index, value ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = value.name,
                                color = themeColor.Text
                            )
                        },
                        onClick = {
                            value.onClick(playlistId)
                            isExpanded = false
                        }
                    )
                    if (index != dropdownList.size - 1) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dimens.Spacing.PlaylistItemDropdownSpace)
                                .background(themeColor.Text)
                        )
                    }
                }
            }
        }
    }
}
