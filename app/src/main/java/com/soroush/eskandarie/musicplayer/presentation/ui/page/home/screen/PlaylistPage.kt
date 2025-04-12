package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.getAlbumArtBitmap
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.MusicItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlin.math.abs

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
@OptIn(ExperimentalFoundationApi::class)
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
    val layoutInfo = lazyState.layoutInfo
    val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == 4 }
    itemInfo?.let {
        val centerOffset = it.offset + it.size / 2
        Log.e("view post", "$centerOffset")
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
                modifier = Modifier
                    .animateItem(
                        fadeInSpec = tween(1500),
                        placementSpec = tween(1500)
                    ),
                colorTheme = colorTheme,
                playlistName = "Rock and Roll baby!!",
                playlistImage = playlistImage
            )
        }
        items(musicList.size, key = { index: Int ->
            musicList[index].hashCode()
        }){ index ->
//            MusicItem(
//                modifier = Modifier
//                    .padding(horizontal = 10.dp)
//                    .animateItem(
//                        fadeInSpec = tween(900),
//                        fadeOutSpec = null,
//                        placementSpec = tween(
//                            durationMillis = 900
//                        )
//                    )
//                ,
//                music = musicList[index],
//                isPlaying = false
//            )
            AnimatedMusicItem(
                music = musicList[index],
                isPlaying = false,
                modifier = Modifier.padding(horizontal = 10.dp)
                    .animateItem(
                        placementSpec = tween(1200)
                    )
            )
        }

    }
    InfiniteListHandler(listState = lazyState) {
        val newItems = loadMoreItems(50, 0)
        musicList += newItems
    }

}
@Composable
fun AnimatedMusicItem(
    music: MusicFile,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    var startAnimation by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -90f,
        animationSpec = tween(durationMillis = 600),
        label = "rotationZ"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    MusicItem(
        music = music,
        isPlaying = isPlaying,
        modifier = modifier
            .graphicsLayer {
                rotationX = rotation
                transformOrigin = TransformOrigin.Center
            }
    )
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