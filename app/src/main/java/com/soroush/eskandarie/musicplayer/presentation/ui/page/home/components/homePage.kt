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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
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
    playlists: List<Playlist>,
    navController: NavController

) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        item{
            FourTopPlaylist(
                modifier = Modifier,
                themeColor = themeColor,
                navController = navController
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
            Text(
                modifier = Modifier,
                fontWeight = FontWeight.Bold,
                text = Constants.HomePageValues.PlaylistSectionTitle,
                color = themeColor.Text,
                style = MaterialTheme
                    .typography
                    .headlineSmall
            )
        }
        item{
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Spacing.HomePageSpaceBetween)
            )
        }
        items(playlists.size){index ->
            PlaylistItem(
                playlistId = playlists[index].id,
                title = playlists[index].name,
                posterUri = playlists[index].poster,
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
    navController: NavController
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
                            navController.navigate
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