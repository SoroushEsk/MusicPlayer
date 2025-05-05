package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.getAlbumArtBitmap
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.nav.Destination
import com.soroush.eskandarie.musicplayer.presentation.state.CurrentPlaylist
import com.soroush.eskandarie.musicplayer.presentation.state.PlaylistType
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.MusicItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import kotlin.math.abs
import kotlin.math.min

@Composable
fun PlaylistPage(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    lazyListState: LazyListState,
    pageDataItem: LazyPagingItems<MusicFile>,
    setState: (action: HomeViewModelSetStateAction) -> Unit,
    isPlaying: Boolean,
    playlistType: PlaylistType,
    playlistOnQueue: CurrentPlaylist
) {
    val lazyState = remember {
        lazyListState
    }
    val context = LocalContext.current
    var playlistImage by remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.empty_album))
    }
    var aspectRatio by remember {
        mutableStateOf(1f)
    }
    var isSelectedModeEnabled by remember {
        mutableStateOf(false)
    }
    val selectedMusic = remember { mutableStateMapOf<String, Boolean>() }
    LaunchedEffect(selectedMusic.size) {
        isSelectedModeEnabled = selectedMusic.isNotEmpty()
    }
    var offsetY by remember{
        mutableStateOf(0f)
    }
    val offsetAnimation by animateFloatAsState(
        targetValue = if(isSelectedModeEnabled) -offsetY else 0f,
        animationSpec = tween(2000)
    )
    val aspectRatioAnimation by animateFloatAsState(
        targetValue = if(isSelectedModeEnabled){
            4f
        }else{
            if (playlistType is PlaylistType.UserPlayList) 1f else 3f
        },
        animationSpec = tween(2000)
    )
    var isLongPress by remember{
        mutableStateOf(false)
    }
    val playlistScope = rememberCoroutineScope()
    LaunchedEffect(pageDataItem.itemCount) {
        if (playlistType is PlaylistType.TopPlaylist) {
            if (playlistType.route == Destination.FavoriteMusicScreen.route) {
                playlistImage = BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.favorite_poster
                )
                return@LaunchedEffect
            }
        }
        if (pageDataItem.itemCount != 0) {
            playlistImage = pageDataItem[0]?.getAlbumArtBitmap() ?: BitmapFactory.decodeResource(
                context.resources,
                R.drawable.empty_album
            )
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorTheme.Background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(colorTheme.Background),
            state = lazyState,
            verticalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatioAnimation)
                        .onGloballyPositioned {
                            offsetY = it.size.width.toFloat()
                            aspectRatio = min(
                                it.size.width / abs(
                                    it.size.height - abs(
                                        it.positionOnScreen().y
                                    )
                                ), if (isSelectedModeEnabled) 4f else 3f
                            )
                        }
                )
            }
            items(pageDataItem.itemCount, key = { index: Int ->
                pageDataItem[index].hashCode()
            }) { index ->
                pageDataItem[index]?.let { musicFile ->
                    AnimatedMusicItem(
                        music = musicFile,
                        isPlaying = false,
                        isSelected = selectedMusic.getOrDefault(musicFile.id.toString(), false),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .animateItem(
                                placementSpec = tween(1200)
                            )
                            .pointerInput(Unit) {

                                detectTapGestures(
                                    onLongPress = {
                                        isLongPress = true
                                        if (selectedMusic.containsKey(musicFile.id.toString())) selectedMusic.remove(
                                            musicFile.id.toString()
                                        )
                                        else selectedMusic.put(musicFile.id.toString(), true)
//                                        setState(
//                                            HomeViewModelSetStateAction.PausePlayback
//                                        )
                                    },
                                    onPress = {
                                        isLongPress = false
                                        awaitRelease()
                                        if (isLongPress) return@detectTapGestures
                                        if (isSelectedModeEnabled) {
                                            if (selectedMusic.containsKey(musicFile.id.toString())) selectedMusic.remove(
                                                musicFile.id.toString()
                                            )
                                            else selectedMusic.put(musicFile.id.toString(), true)
                                        } else {
                                            setState(
                                                HomeViewModelSetStateAction.SetSongToPlay(
                                                    when (playlistType) {
                                                        is PlaylistType.UserPlayList -> PlaylistType.UserPlayList(
                                                            id = playlistType.id,
                                                            name = playlistType.name
                                                        )

                                                        is PlaylistType.TopPlaylist -> PlaylistType.TopPlaylist(
                                                            route = playlistType.route,
                                                            name = playlistType.name
                                                        )

                                                        is PlaylistType.FolderPlaylist -> PlaylistType.FolderPlaylist(
                                                            folderName = playlistType.folderName,
                                                            name = playlistType.name
                                                        )
                                                    },
                                                    musicFile.id
                                                )
                                            )
                                            setState(HomeViewModelSetStateAction.ResumePlayback)
                                        }

                                    }
                                )
                            },
                        colorTheme = colorTheme
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
        selectDetails(
            modifier = Modifier
                .aspectRatio(aspectRatio),
            colorTheme = colorTheme,
            numberSelected = selectedMusic.size,
            isVisible = if(aspectRatioAnimation > 3.4f) true else false
        )
        PlaylistPoster(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = offsetAnimation.dp)
                .aspectRatio(aspectRatio),
            colorTheme = colorTheme,
            playlistType = (playlistType),
            playlistImage = playlistImage,
            setState = setState,
            isPlaying = isPlaying,
            playlistOnQueue = playlistOnQueue
        )
    }
}
@Composable
fun selectDetails(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
    numberSelected: Int,
    isVisible: Boolean,
    leftIcon: ()->Unit = {},
    rightIcon: ()->Unit ={}
){
    if(isVisible) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(colorTheme.Background)
                .statusBarsPadding()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(20.dp),
                imageVector = Icons.Rounded.FavoriteBorder,
                tint = colorTheme.Primary,
                contentDescription = "select all"
            )
            Text(
                text = "$numberSelected",
                color = colorTheme.Primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(20.dp),
                imageVector = Icons.Default.AddCircle,
                tint = colorTheme.Primary,
                contentDescription = "add to playlistIcon"
            )
        }
    }
}
@Composable
fun AnimatedMusicItem(
    music: MusicFile,
    isPlaying: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
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
        isSelected = isSelected,
        modifier = modifier
            .graphicsLayer {
                rotationX = rotation
                transformOrigin = TransformOrigin.Center
            }
    )
}
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun PlaylistPoster(
    modifier: Modifier,
    colorTheme: ColorTheme,
    playlistType: PlaylistType,
    playlistImage: Bitmap,
    setState: (action: HomeViewModelSetStateAction) -> Unit,
    isPlaying: Boolean,
    playlistOnQueue: CurrentPlaylist
) {
    val colortheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme
    val playlistName = when (playlistType) {
        is PlaylistType.TopPlaylist -> playlistType.name
        is PlaylistType.FolderPlaylist -> playlistType.name
        is PlaylistType.UserPlayList -> playlistType.name
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                bitmap = playlistImage.asImageBitmap(),
                contentScale = ContentScale.Crop,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp, end = 10.dp),
                text = playlistName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                ),
                maxLines = 1,
                color = Color.White
            )
            Icon(
                modifier = Modifier
                    .height(72.dp)
                    .aspectRatio(1f)
                    .offset(y = 38.dp)
                    .clip(CircleShape)
                    .background(colorTheme.Secondary)
                    .padding(20.dp)
                    .clickable {
                        if (isThisPlaylistOnQueue(playlistOnQueue, playlistType)) {
                            if (isPlaying)
                                setState(HomeViewModelSetStateAction.PausePlayback)
                            else {
                                setState(HomeViewModelSetStateAction.ResumePlayback)
                            }
                        } else {
                            setState(
                                HomeViewModelSetStateAction.PutPlaylistToQueue(
                                    when (playlistType) {
                                        is PlaylistType.UserPlayList -> PlaylistType.UserPlayList(
                                            id = playlistType.id,
                                            name = playlistType.name
                                        )

                                        is PlaylistType.TopPlaylist -> PlaylistType.TopPlaylist(
                                            route = playlistType.route,
                                            name = playlistType.name
                                        )

                                        is PlaylistType.FolderPlaylist -> PlaylistType.FolderPlaylist(
                                            folderName = playlistType.folderName,
                                            name = playlistType.name
                                        )
                                    }
                                )
                            )
                        }
                    },
                tint = Color.White,
                painter = painterResource(
                    id = if (isThisPlaylistOnQueue(
                            playlistOnQueue,
                            playlistType
                        )
                    ) playIcon(isPlaying) else R.drawable.play_button
                ),
                contentDescription = "Playlist_page_Play_button"
            )
        }
    }
}

private fun playIcon(isPlaying: Boolean) =
    if (isPlaying) R.drawable.pause else R.drawable.play_button

private fun isThisPlaylistOnQueue(
    playlistOnQueue: CurrentPlaylist,
    playlistType: PlaylistType
): Boolean {
    return when (playlistType) {
        is PlaylistType.TopPlaylist -> {
            if (playlistOnQueue.route == playlistType.route) true
            else false
        }

        is PlaylistType.FolderPlaylist -> {
            if (playlistOnQueue.route == Destination.FolderMusicScreen.route) {
                if (playlistOnQueue.parameter == playlistType.folderName) true else false
            } else false
        }

        is PlaylistType.UserPlayList -> {
            if (playlistOnQueue.route == Destination.PlaylistScreen.route) {
                if (playlistOnQueue.parameter == playlistType.id.toString()) true else false
            } else false
        }
    }
}