package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.getAlbumArtBitmap
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.MusicItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.withContext

@Preview
@Composable
fun start(){
    PlaylistPage(
        loadMoreItems = { count, start ->
            List(50) {
                MusicFile(
                    id = System.currentTimeMillis() + it,
                    title = "Z - The warning",
                    artist = "The warning",
                    album = "",
                    duration = 213443,
                    recordingDate = null,
                    genre = null,
                    size = 234,
                    path = "/storage/emulated/0/Download/NeginKt - Paiz   Saraabe Toe.mp3"
                )
            }
        }
    )
}
@Composable
fun PlaylistPage(
    modifier: Modifier = Modifier,
    loadMoreItems: (count: Int, start: Int)->List<MusicFile>,
    colorTheme: ColorTheme = if(isSystemInDarkTheme()) DarkTheme else LightTheme,
    lazyState: LazyListState = rememberLazyListState()
) {
    var musicList by remember {
        mutableStateOf(
            listOf<MusicFile>()
        )
    }
    val context = LocalContext.current
    val playlistImage = remember(musicList) {
        if (musicList.isEmpty()) {
            BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
        } else {
            musicList[0].getAlbumArtBitmap()?:BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorTheme.Background),
        state = lazyState,
        verticalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        item {
            PlaylistPoster(
                modifier = Modifier,
                colorTheme = colorTheme,
                playlistName = "Rock and Roll baby!!",
                playlistImage = playlistImage
            )
        }
        items(musicList.size, key = { index: Int ->
            musicList[index].hashCode()
        }){ index ->
            MusicItem(
                modifier = Modifier.padding(horizontal = 10.dp),
                music = musicList[index],
                isPlaying = false
            )
        }

    }
    InfiniteListHandler(listState = lazyState) {
        val newItems = loadMoreItems(50, 0)
        musicList += newItems
    }

}
@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 5,
    onLoadMore: () -> Unit
) { 
    val shouldLoadMore = remember {
        derivedStateOf { 
            val totalItemsCount = listState.layoutInfo.totalItemsCount 
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 
            lastVisibleItemIndex >= (totalItemsCount - buffer)
        }
    }
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onLoadMore()
            }
    }
}
@Composable
fun PlaylistPoster(
    modifier: Modifier,
    colorTheme: ColorTheme,
    playlistName: String,
    playlistImage: Bitmap
){
     Box(modifier = modifier
         .fillMaxWidth()
         .aspectRatio(1f),
         contentAlignment = Alignment.BottomStart
     ){
         Box(
             modifier = Modifier
                 .fillMaxSize(),
             contentAlignment = Alignment.Center
         ){
             Image(
                 modifier = Modifier.fillMaxSize(),
                 bitmap = playlistImage.asImageBitmap(),
                 contentDescription = "playlist_poster_image"
             )
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(
                         brush = Brush.verticalGradient(
                             colors = listOf(
                                 Color.Transparent,
                                 colorTheme.DarkShadow
                             )
                         )
                     )
             )
         }
         Text(
             modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
             text = playlistName,
             style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 36.sp),
             maxLines = 1,
             color = Color.White
         )
     }
}