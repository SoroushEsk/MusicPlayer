package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction
import com.soroush.eskandarie.musicplayer.presentation.nav.Destination
import com.soroush.eskandarie.musicplayer.presentation.state.FourTopPlaylistImageState
import com.soroush.eskandarie.musicplayer.presentation.state.PlaylistType
import com.soroush.eskandarie.musicplayer.presentation.ui.model.PlaylistDropdownItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.util.Constants
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    loadPlaylist: State<List<Playlist>>,
    navigate: (action: NavControllerAction) -> Unit,
    setState: (action: HomeViewModelSetStateAction) -> Unit,
    getState: (action: HomeViewModelGetStateAction) -> StateFlow<*>
) {
    setState(HomeViewModelSetStateAction.UpdateTopPlaylistState)
    val topPlaylistState by (getState(HomeViewModelGetStateAction.GetTopPlaylistState).collectAsState() as State<FourTopPlaylistImageState>)
    val playlistList by loadPlaylist
    val lazyListState = rememberLazyListState()
    var textEditInput by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            state = lazyListState
        ) {
            item {
                FourTopPlaylist(
                    modifier = Modifier,
                    themeColor = colorTheme,
                    navigate = navigate,
                    state = topPlaylistState
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Spacing.HomePageSpaceBetween)
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(Dimens.Size.HomePagePlaylistTitleContainerHeight)
                ) {
                    Text(
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        text = Constants.HomePageValues.PlaylistSectionTitle,
                        color = colorTheme.Text,
                        style = MaterialTheme
                            .typography
                            .headlineSmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .padding(Dimens.Padding.HomePageAddPlaylistIcon)
                            .aspectRatio(Dimens.AspectRatio.AddNewPlaylistButton)
                            .fillMaxHeight()
                            .clickable {
                                showDialog = true
                            },
                        painter = painterResource(id = R.drawable.add_to_playlist),
                        contentDescription = Constants.HomePageValues.AddPlaylistIconDescription,
                        tint = colorTheme.Text
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Spacing.HomePageSpaceBetween)
                )
            }
            item {
                PlaylistItem(
                    playlistId = -1,
                    title = Constants.HomePageValues.Favorite,
                    posterBitmap = BitmapFactory.decodeResource(
                        LocalContext.current.resources,
                        R.drawable.favorite_playlist
                    ),
                    posterShape = RoundedCornerShape(12.dp),
                    onIcon1Click = {
                    },
                    onIcon2Click = { },
                    dropdownList = listOf(
                        PlaylistDropdownItem(0, "Rename") {},
                        PlaylistDropdownItem(1, "Delete") {},
                        PlaylistDropdownItem(2, "Share") {},
                        PlaylistDropdownItem(3, "Add") {}
                    ),
                ) {
                    navigate(NavControllerAction.NavigateToFavorite(Destination.FavoriteMusicScreen.route))
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Spacing.HomePageSpaceBetween)
                )
            }
            items(playlistList.size) { index ->
                PlaylistItem(
                    playlistId = playlistList[index].id,
                    title = playlistList[index].name,
                    posterBitmap = MusicFile.getAlbumArtBitmap(
                        playlistList[index].poster,
                        LocalContext.current
                    ),
                    posterShape = RoundedCornerShape(12.dp),
                    onIcon1Click = {
                    },
                    onIcon2Click = { },
                    dropdownList = listOf(
                        PlaylistDropdownItem(0, "Rename") {},
                        PlaylistDropdownItem(1, "Delete") {},
                        PlaylistDropdownItem(2, "Share") {},
                        PlaylistDropdownItem(3, "Add") {}
                    )
                ) {
                    navigate(
                        NavControllerAction.NavigateToPlaylist(
                            playlistList[index].id,
                            Destination.PlaylistScreen.route,
                            playlistList[index].name
                        )
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Spacing.HomePageSpaceBetween)
                )
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.Spacing.MusicBarMotionLayoutContainerHeight)
                )
            }
        }
        if (showDialog) {
            //Todo("add persian support")
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = "Playlist's name")
                },
                text = {
                    TextField(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = Dimens.CornerRadius.TextFieldAddPlaylist,
                                    topEnd = Dimens.CornerRadius.TextFieldAddPlaylist
                                )
                            ),
                        value = textEditInput,
                        maxLines = 1,
                        onValueChange = { textEditInput = it },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = colorTheme.Text,
                            focusedContainerColor = colorTheme.Background,
                            unfocusedContainerColor = colorTheme.Background,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = colorTheme.FocusedField,
                            focusedIndicatorColor = colorTheme.Primary,
                            unfocusedIndicatorColor = colorTheme.Background,
                            focusedLabelColor = colorTheme.FocusedField,
                            focusedPrefixColor = colorTheme.FocusedField,
                        ),

                        )
                },
                confirmButton = {
                    TextButton(onClick = {
                        setState(HomeViewModelSetStateAction.AddANewPlaylist(textEditInput))
                        showDialog = false
                        textEditInput = ""
                    }
                    ) {
                        Text(text = "Submit", color = colorTheme.Text)

                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        textEditInput = ""
                    }) {
                        Text(text = "Cancel", color = colorTheme.Text)
                    }
                },
                containerColor = colorTheme.Surface,
                textContentColor = colorTheme.Background,
                titleContentColor = colorTheme.Text
            )
        }
    }

}

@Composable
fun FourTopPlaylist(
    modifier: Modifier = Modifier,
    themeColor: ColorTheme,
    navigate: (action: NavControllerAction) -> Unit,
    state: FourTopPlaylistImageState
) {
    val defaultBitmap =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.empty_album)
    val front = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.folder1)
    val left = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.folder2)
    val right = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.folder3)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val playlistModifier = Modifier
            .padding(0.dp, Dimens.Padding.HomePagePadding)
            .clip(RoundedCornerShape(16.dp))
            .background(themeColor.DarkSurface)
            .fillMaxSize()
        val rowModifier = Modifier
            .fillMaxWidth()
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = rowModifier
                    .weight(1f)
            ) {
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ) {
                    TopPlaylistItem(
                        modifier = Modifier.clickable {
                            navigate(NavControllerAction.NavigateToFolders(Destination.FolderScreen.route))
                        },
                        title = "Folders",
                        bitmapFront = front,
                        bitmapBack1 = left,
                        bitmapBack2 = right,
                        extraPadding = 12.dp
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(Dimens.Padding.HomePagePadding * 2)
                )
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ) {

                    TopPlaylistItem(
                        modifier = Modifier.clickable {
                            navigate(NavControllerAction.NavigateToAllMusic(Destination.AllMusicScreen.route))
                        },
                        title = state.AllMusic.name,
                        bitmapFront = state.AllMusic.front ?: defaultBitmap,
                        bitmapBack1 = state.AllMusic.back_right ?: defaultBitmap,
                        bitmapBack2 = state.AllMusic.back_left ?: defaultBitmap,
                        extraPadding = 12.dp
                    )
                }
            }
            Row(
                modifier = rowModifier
                    .weight(1f)
            ) {
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ) {
                    TopPlaylistItem(
                        modifier = Modifier.clickable {
                            navigate(NavControllerAction.NavigateToRecentlyPlayed(Destination.RecentlyPlayedScreen.route))
                        },
                        title = state.RecentrlyPlayed.name,
                        bitmapFront = state.RecentrlyPlayed.front ?: defaultBitmap,
                        bitmapBack1 = state.RecentrlyPlayed.back_right ?: defaultBitmap,
                        bitmapBack2 = state.RecentrlyPlayed.back_left ?: defaultBitmap,
                        extraPadding = 12.dp
                    )

                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(Dimens.Padding.HomePagePadding * 2)
                )
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ) {

                    TopPlaylistItem(
                        modifier = Modifier.clickable {
                            navigate(NavControllerAction.NavigateToMostPlayed(Destination.MostPlayedScreen.route))
                        },
                        title = state.MostPlayed.name,
                        bitmapFront = state.MostPlayed.front ?: defaultBitmap,
                        bitmapBack1 = state.MostPlayed.back_left ?: defaultBitmap,
                        bitmapBack2 = state.MostPlayed.back_right ?: defaultBitmap,
                        extraPadding = 12.dp
                    )
                }
            }
        }
    }

}
