package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import android.net.Uri
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction
import com.soroush.eskandarie.musicplayer.presentation.nav.Destination
import com.soroush.eskandarie.musicplayer.presentation.ui.model.PlaylistDropdownItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.util.Constants

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    themeColor: ColorTheme = if(isSystemInDarkTheme()) DarkTheme else LightTheme,
    loadPlaylist: State<List<Playlist>>,
    navigate: (action: NavControllerAction)->Unit,
    setState: (action: HomeViewModelSetStateAction) -> Unit,
) {
    val playlistList by loadPlaylist
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        state = lazyListState
    ) {
        item{
            FourTopPlaylist(
                modifier = Modifier,
                themeColor = themeColor,
                navigate = navigate
            )
        }
        item{
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Spacing.HomePageSpaceBetween)
            )
        }
        item{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(Dimens.Size.HomePagePlaylistTitleContainerHeight)
            ) {
                Text(
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    text = Constants.HomePageValues.PlaylistSectionTitle,
                    color = themeColor.Text,
                    style = MaterialTheme
                        .typography
                        .headlineSmall
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .padding(Dimens.Padding.HomePageAddPlaylistIcon)
                        .aspectRatio(Dimens.AspectRatio.AddNewPlaylistButton).fillMaxHeight(),
                    painter = painterResource(id = R.drawable.add_to_playlist),
                    contentDescription = Constants.HomePageValues.AddPlaylistIconDescription,
                    tint = themeColor.Text
                )
            }
        }
        item{
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Spacing.HomePageSpaceBetween)
            )
        }
        items(playlistList.size){index ->
            PlaylistItem(
                playlistId = playlistList[index].id,
                title = playlistList[index].name,
                posterBitmap = MusicFile.getAlbumArtBitmap( playlistList[index].poster, LocalContext.current),
                posterShape = RoundedCornerShape(12.dp),
                onIcon1Click = { },
                onIcon2Click = { },
                dropdownList = listOf(
                    PlaylistDropdownItem(0, "Rename"){},
                    PlaylistDropdownItem(1, "Delete") {},
                    PlaylistDropdownItem(2, "Share"){},
                    PlaylistDropdownItem(3,"Add"){}
                )
            ) {
                navigate(NavControllerAction.NavigateToPlaylist(playlistList[index].id, Destination.PlaylistScreen.route))
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Spacing.HomePageSpaceBetween)
            )
        }
    }

}

@Composable
fun FourTopPlaylist(
    modifier: Modifier = Modifier,
    themeColor : ColorTheme,
    navigate: (action: NavControllerAction)->Unit,
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .aspectRatio(1f)
    ){
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
        ){
            Row (
                modifier = rowModifier
                    .weight(1f)
            ){
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ){
                    TopPlaylistItem(
                        title = "Folders",
                        uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.ed),
                        uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.mahasti),
                        uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.sandi),
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
                ){

                    TopPlaylistItem(
                        modifier = Modifier.clickable {
                            navigate(NavControllerAction.NavitateToAllMusic(Destination.AllMusicScreen.route))
                        },
                        title = "All Songs",
                        uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.sandi),
                        uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.mahasti),
                        uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.simint),
                        extraPadding = 12.dp
                    )
                }
            }
            Row (
                modifier = rowModifier
                    .weight(1f)
            ){
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ){
                    TopPlaylistItem(
                        title = "Recently Played",
                        uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.simint),
                        uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.ghader),
                        uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.sandi),
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
                ){

                    TopPlaylistItem(
                        title = "Most Played",
                        uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.shaj),
                        uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.mahasti),
                        uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.sharhram),
                        extraPadding = 12.dp
                    )
                }
            }
        }
    }
}
