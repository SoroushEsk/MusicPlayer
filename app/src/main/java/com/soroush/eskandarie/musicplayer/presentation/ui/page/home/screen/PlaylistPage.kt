package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.getAlbumArtBitmap
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.MusicItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun PlaylistPage(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme = if(isSystemInDarkTheme()) DarkTheme else LightTheme,
    lazyListState: LazyListState,
    pageDataItem: LazyPagingItems<MusicFile>,
    setState: (action: HomeViewModelSetStateAction)->Unit
) {
    val lazyState = remember {
        lazyListState
    }
    val context = LocalContext.current
    var playlistImage by remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.empty_album))
    }
    LaunchedEffect(pageDataItem.itemCount) {
        if(pageDataItem.itemCount != 0){
            playlistImage = pageDataItem[0]?.getAlbumArtBitmap()?:BitmapFactory.decodeResource(context.resources, R.drawable.empty_album)
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
                modifier = Modifier
                    .animateItem(
                        placementSpec = tween(1000)
                    ),
                colorTheme = colorTheme,
                playlistName = "Rock and Roll baby!!",
                playlistImage = playlistImage
            )
        }
        items(pageDataItem.itemCount, key = { index: Int ->
            pageDataItem[index].hashCode()
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

            pageDataItem[index]?.let {
                AnimatedMusicItem(
                    music = it,
                    isPlaying = false,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .animateItem(
                            placementSpec = tween(1200)
                        )
                )
            }

        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Spacing.MusicBarMotionLayoutContainerHeight)
            )
        }

    }

}
@Composable
fun AnimatedMusicItem(
    music: MusicFile,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    shpae: Shape = RoundedCornerShape(Dimens.CornerRadius.FolderItem)
) {
    var startAnimation by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -45f,
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