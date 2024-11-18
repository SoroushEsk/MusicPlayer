package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme

@Composable
fun HomePlaylist(
    modifier: Modifier = Modifier,
    themeColor: ColorTheme = if(isSystemInDarkTheme()) DarkTheme else LightTheme



) {
    var scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        FourTopPlaylist(
            modifier = Modifier,
            themeColor = themeColor
        )

    }
}

@Composable
fun FourTopPlaylist(
    modifier: Modifier = Modifier,
    themeColor : ColorTheme
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(4.dp)
    ){
        val playlistModifier = Modifier
            .padding(10.dp)
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
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ){

                    TopPlaylistItem(
                        title = "All Songs",
                        uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.ed),
                        uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.mahasti),
                        uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.sandi),
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
                        uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.ed),
                        uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.mahasti),
                        uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.sandi),
                        extraPadding = 12.dp
                    )

                }
                Box(
                    modifier = playlistModifier
                        .weight(1f)
                ){

                    TopPlaylistItem(
                        title = "Most Played",
                        uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.ed),
                        uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.mahasti),
                        uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/" + R.drawable.sandi),
                        extraPadding = 12.dp
                    )
                }
            }
        }
    }
}