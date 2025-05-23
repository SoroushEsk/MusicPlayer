package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.palette.graphics.Palette
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.PlaybackState
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.extensions.getReadableTextColor
import com.soroush.eskandarie.musicplayer.presentation.state.PlaybackStates
import com.soroush.eskandarie.musicplayer.presentation.ui.MusicPageScrollState
import com.soroush.eskandarie.musicplayer.presentation.ui.animation.musicPageMotionLayoutConfig
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.floor

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MusicPage(
    modifier: Modifier = Modifier,
    setState: (action: HomeViewModelSetStateAction)->Unit,
    getState: (action: HomeViewModelGetStateAction)->StateFlow<*>,
    colorTheme: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    posterShape: Shape = RoundedCornerShape(Dimens.CornerRadius.General),
    onClick: (playbackState: PlaybackState) -> Unit
) {
    val playbackState by (getState(HomeViewModelGetStateAction.GetMusicStatus)as StateFlow<PlaybackStates>).collectAsState()
    val configuration = LocalConfiguration.current
    var progress by rememberSaveable { mutableStateOf(0.0f) }
    var scrollStatus by remember { mutableStateOf(MusicPageScrollState.NoScroll) }
    val bitmap = playbackState.bitmapBitmap
    val palette = Palette.from(bitmap).generate()

    var posterCoordinates by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var mainContainerCoordinates by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    val dominantColor1 = Color(palette.getDominantColor(0)).copy(alpha = Dimens.Alpha.DomainColor1Alpha)
    val dominantColor2 = Color(palette.getDominantColor(0)).copy(alpha = Dimens.Alpha.DomainColor2Alpha)
    val vibrantColor1 = Color(palette.getVibrantColor(0)).copy(alpha = Dimens.Alpha.VibrantColor1Alpha)
    val mutedColor = Color(palette.getMutedColor(0)).copy(alpha = Dimens.Alpha.MutedColorAlpha)
    val listOfColors =
        listOf(dominantColor1, dominantColor2, vibrantColor1, mutedColor)
    val radialGradientBrush = Brush.radialGradient(
        colors = listOfColors,
        center = Offset(posterCoordinates.x, posterCoordinates.y - mainContainerCoordinates.y),
        radius = Math.sqrt(
            Math.pow(
                (LocalDensity.current.density * configuration.screenHeightDp - posterCoordinates.y).toDouble(),
                2.0
            ) + Math.pow(
                (LocalDensity.current.density * configuration.screenWidthDp - posterCoordinates.x).toDouble(),
                2.0
            )
        ).toFloat()
    )
    var fingerX by remember { mutableStateOf(0f) }
    var isTouching by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = when( scrollStatus ) {
            MusicPageScrollState.ScrollUp -> 1f
            MusicPageScrollState.ScrollDown -> 0f
            else -> progress
        },
        animationSpec = tween(
            durationMillis =  Constants.MusicPageValues.LeaveScrollDuration
        )
    )
    progress = animatedProgress

    var textWidth by remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition()
    var musicBarPosterWidth by remember { mutableStateOf(1f) }
    var isTheSameSize by remember { mutableStateOf(false)}
    val pagePosterAlphaAnimation by animateFloatAsState(
        targetValue = if (isTheSameSize) 1f else 0f,
        animationSpec = tween(durationMillis = if( progress == 1f || progress == 0f ) 0 else Constants.MusicPageValues.DiskAndNeedleRevealDuration)
    )
    val barPosterAlphaAnimation by animateFloatAsState(
        targetValue = if (isTheSameSize) 1f else 0f,
        animationSpec = tween(durationMillis = if( progress == 1f || progress == 0f ) 0 else Constants.MusicBarValues.PosterHideDuration)
    )
    var offset by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y != 0f) {
                }
                return Offset.Zero
            }
        }
    }

    var horizontalScrollPrevId by remember{
        mutableStateOf(-1L)
    }
    MotionLayout(
        motionScene = MotionScene(content = musicPageMotionLayoutConfig),
        progress = animatedProgress,
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxHeight()
            .fillMaxWidth()

    ) {
        Column(
            modifier = modifier
                .layoutId(Constants.MusicBarValues.MotionLayoutContainerId)
                .background(Color.Transparent)
                .clip(
                    RoundedCornerShape(
                        topStart = if (progress < 1f) Dimens.CornerRadius.General else 0.dp,
                        topEnd = if (progress < 1f) Dimens.CornerRadius.General else 0.dp
                    )
                )
                .background(colorTheme.Background)
                .nestedScroll(nestedScrollConnection)
                .background(radialGradientBrush)
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollableState { delta ->
                        offset += delta
                        if (delta > 100) scrollStatus = MusicPageScrollState.ScrollDown
                        else if (delta < -2) scrollStatus = MusicPageScrollState.ScrollUp
                        delta
                    }
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (change.id.value == horizontalScrollPrevId) return@detectHorizontalDragGestures
                        horizontalScrollPrevId = change.id.value
                        if (dragAmount < -10) setState(HomeViewModelSetStateAction.ForwardPlayback)
                        else if (dragAmount > 90) setState(HomeViewModelSetStateAction.BackwardPlayback)
                    }
                }
                .onGloballyPositioned { coordinates ->
                    mainContainerCoordinates =
                        Offset(coordinates.positionInWindow().x, coordinates.positionInWindow().y)
                }
            ) {

        }
        Column(
            modifier = Modifier
                .layoutId(Constants.MusicBarValues.MotionLayoutTextContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = if(progress == 0f) Alignment.Start else Alignment.CenterHorizontally
        ) {
            Text(
                text = playbackState.title,
                modifier = modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutTitleId),
                color = colorTheme.Text.copy(alpha = 0.65f),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                onTextLayout = { textLayoutResult ->
                    textWidth = textLayoutResult.size.width.toFloat()
                }
            )
            Text(
                text = playbackState.artist,
                modifier = modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutArtistId),
                color = colorTheme.Text.copy(alpha = 0.4f),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
        IconsAtEndsRow(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.DownOptionIconContainerId),
            rightIcon =  R.drawable.options_list,
            leftIcon  =  R.drawable.down_arrow,
            colorTheme = colorTheme,
            progress = progress,
            onClickLeft = {
                scrollStatus = MusicPageScrollState.ScrollDown
            }
        )
        Image(
            bitmap = playbackState.bitmapBitmap.asImageBitmap(),
            contentDescription = Constants.MusicPageValues.MusicPosterDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .alpha(barPosterAlphaAnimation)
                .layoutId(Constants.MusicBarValues.MotionLayoutPosterId)
                .padding(Dimens.Padding.MusicBarMusicPoster)
                .aspectRatio(1f)
                .clip(posterShape)
                .onGloballyPositioned { layoutCoordinates ->
                    musicBarPosterWidth = layoutCoordinates.size.width.toFloat()
                }
                .then(if (progress == 1f) Modifier.size(0.dp) else Modifier)
        )
        SongPoster(
            modifier = Modifier
                .alpha(1f - pagePosterAlphaAnimation)
                .onGloballyPositioned { coordinates ->
                    val width = coordinates.size.width.toFloat()
                    val height = coordinates.size.height.toFloat()
                    val offsetX = coordinates.positionInWindow().x
                    val offsetY = coordinates.positionInWindow().y
                    posterCoordinates = Offset(offsetX + width / 2, offsetY + height / 2)
                    if ((width * 0.8f * Constants.MusicPageValues.MusicPosterToDiskRatio) >= musicBarPosterWidth) {
                        isTheSameSize = false
                    } else isTheSameSize = true
                },
            poster = playbackState.bitmapBitmap,
            discImage = if(isSystemInDarkTheme())R.drawable.gramaphone_disc else R.drawable.disk_light,
            needleImage = if(isSystemInDarkTheme()) R.drawable.needle else R.drawable.needle_light ,
            isAnimation = progress == 1f,
            resetRotation = progress == 0f,
            colorTheme = colorTheme,
            rotationPercent = (playbackState.currentDuration).toFloat()/60000
        )
        IconsAtEndsRow(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.AboveProgressBarContainerId),
            rightIcon =  R.drawable.filled_heart,
            leftIcon  =  R.drawable.playlist,
            lightTint = colorTheme.FavoriteTint,
            colorTheme = colorTheme,
            isColorTint = playbackState.isFavorite,
            progress = progress,
            onClickRight = {
                if(playbackState.isFavorite) setState(HomeViewModelSetStateAction.ChangeFavoriteState(false))
                else setState(HomeViewModelSetStateAction.ChangeFavoriteState(true))
            }
        )
        ProgressBar(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.PlayProgressBarContainerId)
                .fillMaxWidth()
                .fillMaxHeight(0.05f),
            colorTheme = colorTheme,
            currentPosition = playbackState.currentDuration,
            totalDuration = playbackState.totalDuration,
            songPercent = playbackState.musicPercent,
            onProgressChange = {newProgress ->
                setState(HomeViewModelSetStateAction.ChangeMusicPositionTo(newProgress))
            }
        )
        PLayControlShadow(
            modifier = modifier.rotate(90f),
            tint = colorTheme.Background,
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 5.dp + (progress * 7.dp.value).dp,
            layoutId = Constants.MusicBarValues.MotionLayoutBackIconId
        ){
            setState(HomeViewModelSetStateAction.BackwardPlayback)
        }
        PLayControlShadow(
            modifier = Modifier,
            tint = colorTheme.Background,
            iconPainter = if (playbackState.isPlaying.not()) R.drawable.play_button else R.drawable.pause,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 10.dp + (progress * 10.dp.value).dp,
            layoutId = Constants.MusicBarValues.MotionLayoutPlayIconId
        ){
            if(playbackState.isPlaying) setState(HomeViewModelSetStateAction.PausePlayback)
            else  setState(HomeViewModelSetStateAction.ResumePlayback)
        }
        PLayControlShadow(
            modifier = modifier,
            tint = colorTheme.Background,
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 5.dp + (progress * 7.dp.value).dp,
            layoutId = Constants.MusicBarValues.MotionLayoutForwardIconId
        ){
            setState(HomeViewModelSetStateAction.ForwardPlayback)
        }
        IconsAtEndsRowRepeatShuffle(
            modifier  = Modifier
                .layoutId(Constants.MusicPageValues.ShuffleRepeatContainerId),
            isColorTint = playbackState.isShuffle,
            colorTheme = colorTheme,
            progress = progress,
            setState = setState,
            playbackState = playbackState
        )
        Icon(
            painter = painterResource(id = R.drawable.playlist),
            contentDescription = Constants.MusicPageValues.MusicPosterDescription,
            modifier = Modifier
                .layoutId(Constants.MusicBarValues.MotionLayoutPlaylistIconId)
                .padding(7.dp)
                .aspectRatio(1f),
            tint = colorTheme.Text.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun PLayControlShadow(
    modifier: Modifier,
    iconPainter: Int,
    shape: Shape = RoundedCornerShape(Dimens.CornerRadius.MusicPagePlayControlShadowIconDefault),
    colorTheme: ColorTheme = DarkTheme,
    aspectRatio: Float = 1f,
    backgroundColor: Color = Color.Transparent,
    tint: Color = colorTheme.DarkSurface,
    padding: Dp = Dimens.Padding.MusicPagePlayControlShadowDefault,
    layoutId: String = "",
    onClick: ()->Unit = {}
) {
    Box(
        modifier = modifier
            .layoutId(layoutId)
            .fillMaxHeight()
            .aspectRatio(aspectRatio)
            .fillMaxWidth()
            .clip(shape)
            .shadow(
                elevation = Dimens.Elevation.MusicPagePlayControlShadowDarkLarge,
                shape = shape,
                clip = true,
                spotColor = colorTheme.DarkShadow
            )
            .shadow(
                elevation = Dimens.Elevation.MusicPagePlayControlShadowDarkMedium,
                shape = shape,
                clip = true,
                spotColor = colorTheme.DarkShadow
            )
            .background(backgroundColor)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconPainter),
            contentDescription = null,
            tint = tint,
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}
@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
    currentPosition: Long,
    totalDuration: Long,
    songPercent: Float,
    padding: PaddingValues = PaddingValues(
        horizontal = Dimens.Padding.MusicPageProgressBarDefaultHorizontal,
        vertical = Dimens.Padding.MusicPageProgressBarDefaultVertical
    ),
    onProgressChange: (newPercent: Float)->Unit
) {
    val currentPositionMin = (currentPosition/1000)/60
    val currentPositionSecond = (currentPosition/1000)%60
    val totalDurationMin = (totalDuration/1000)/60
    val totalDurationSecond = (totalDuration/1000)%60

    val currentPositionString =
        if (currentPositionMin < 10) {
            "0$currentPositionMin"
        }else {
            "$currentPositionMin"
        } + ":" +
        if(currentPositionSecond < 10 ) {
            "0$currentPositionSecond"
        }else {
            "$currentPositionSecond"
        }
    val totalDurationString =
        if (totalDurationMin < 10) {
            "0$totalDurationMin"
        }  else{ "$totalDurationMin" } + ":" +
        if(totalDurationSecond < 10 ) {
            "0$totalDurationSecond"
        } else {
            "$totalDurationSecond"
        }

    var barWidth by remember { mutableStateOf(1f) }
    var newProgress by remember { mutableStateOf(1f) }
    Row(
        modifier = modifier
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentPositionString,
            color = colorTheme.Text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = Dimens.Padding.MusicPageProgressBarCanvasHorizontal)
                .onGloballyPositioned {
                    barWidth = it.size.width.toFloat()
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val barWidthPx = barWidth
                        newProgress = (down.position.x / barWidthPx).coerceIn(0f, 1f)
                        onProgressChange(newProgress)
                        drag(down.id) { change ->
                            change.consume()
                            newProgress = (change.position.x / barWidthPx).coerceIn(0f, 1f)
                            onProgressChange(newProgress)
                        }
                    }
                }

        ) {
            if (size.width > 0 && size.height > 0) {
                drawLine(
                    color = colorTheme.LightShadow,
                    start = Offset(0f, center.y),
                    end = Offset(size.width, center.y),
                    strokeWidth = Dimens.Size.MusicPageProgressBarStrokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(colorTheme.Icon, colorTheme.Secondary),
                        start = Offset(center.x, center.y),
                        end = Offset(size.width * songPercent, center.y)
                    ),
                    start = Offset(0f, center.y),
                    end = Offset(size.width * songPercent, center.y),
                    strokeWidth = Dimens.Size.MusicPageProgressBarStrokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
                drawPoints(
                    points = listOf(Offset(size.width * songPercent, center.y)),
                    pointMode = PointMode.Points,
                    color = colorTheme.Secondary,
                    strokeWidth = (
                            Dimens.Size.MusicPageProgressBarStrokeWidth.value *
                                    Constants.MusicPageValues.ProgressBarPointToStrokeRatio).dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        Text(
            text = totalDurationString,
            color = colorTheme.Text
        )
    }
}
@Composable
fun IconsAtEndsRow(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
    rightIcon: Int,
    leftIcon: Int,
    progress: Float,
    lightTint: Color = colorTheme.Secondary,
    isColorTint: Boolean = false,
    extraPadding: Dp = Dimens.Padding.MusicPageIconsAtEndRowDefault,
    onClickRight: () -> Unit = {},
    onClickLeft: () -> Unit = {}
) {
    var size by remember {
        mutableStateOf(Size.Zero)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(extraPadding)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.toSize()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if ((size.width > 50 && size.height > 50) || progress > 0.6f){

            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f),
                iconPainter = leftIcon,
                colorTheme = colorTheme,
                onClick = onClickLeft
            )
            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f),
                iconPainter = rightIcon,
                tint = if(isColorTint) lightTint else colorTheme.Tint,
                colorTheme = colorTheme,
                onClick = onClickRight
            )
        }
    }
}@Composable
fun IconsAtEndsRowRepeatShuffle(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
    progress: Float,
    lightTint: Color = colorTheme.Secondary,
    isColorTint: Boolean = false,
    extraPadding: Dp = Dimens.Padding.MusicPageIconsAtEndRowDefault,
    setState: (action: HomeViewModelSetStateAction) -> Unit,
    playbackState: PlaybackStates
) {
    var size by remember {
        mutableStateOf(Size.Zero)
    }
    val rightIcon = R.drawable.shuffle
    val noRepeat = R.drawable.repeat_disable
    val repeatList = R.drawable.list_repeat
    val repeatOnce = R.drawable.repeat_once

    val repeatModeNoRepeat   = com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode.No_Repeat
    val repeatModeRepeatAll  = com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode.Repeat_All
    val repeatModeRepeatOnce = com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode.Repeat_Once
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(extraPadding)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.toSize()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if ((size.width > 50 && size.height > 50) || progress > 0.6f){

            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f),
                iconPainter = when(playbackState.repeatMode){
                     repeatModeRepeatAll -> repeatList
                     repeatModeRepeatOnce-> repeatOnce
                     repeatModeNoRepeat  -> noRepeat
                    else -> noRepeat
                },
                colorTheme = colorTheme,
                onClick = {
                    when(playbackState.repeatMode){
                        com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode.Repeat_All -> {
                            setState(HomeViewModelSetStateAction.SetRepeatMode(repeatModeNoRepeat))
                        }
                        com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode.Repeat_Once-> {
                            setState(HomeViewModelSetStateAction.SetRepeatMode(repeatModeRepeatAll))
                        }
                        com.soroush.eskandarie.musicplayer.presentation.state.RepeatMode.No_Repeat  -> {
                            setState(HomeViewModelSetStateAction.SetRepeatMode(repeatModeRepeatOnce))
                        }
                    }
                }
            )
            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f)
                    .then(
                        if (isColorTint) Modifier
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(lightTint.copy(alpha = 0.2f), Color.Transparent)
                                )
                            )
                        else Modifier
                    )
                    ,
                iconPainter = rightIcon,
                tint = if(isColorTint) lightTint else colorTheme.Tint,
                colorTheme = colorTheme,
                onClick = {
                    if(playbackState.isShuffle) setState(HomeViewModelSetStateAction.SetShuffleState(false))
                    else setState(HomeViewModelSetStateAction.SetShuffleState(true))
                }
            )
        }
    }
}
@Composable
fun SongPoster(
    modifier: Modifier,
    poster: Bitmap,
    discImage: Int,
    needleImage: Int,
    isAnimation: Boolean,
    resetRotation: Boolean,
    colorTheme: ColorTheme,
    rotationPercent: Float,
    rotationLength: Int = Constants.MusicPageValues.DiskRotationDuration
) {
    var preRotatePercent by remember{
        mutableStateOf(0f)
    }
    var onMusicChange by remember{
        mutableStateOf(false)
    }
    val animatedRotation by animateFloatAsState(
        targetValue = (if(preRotatePercent>rotationPercent) floor(preRotatePercent.toDouble()).toFloat() else rotationPercent)*360f,
        animationSpec = tween(durationMillis = 1150, easing = LinearEasing),
        label = "RotationAnimation"
    )
    LaunchedEffect(key1 = rotationPercent) {
        onMusicChange = if(preRotatePercent>rotationPercent) true
        else false
        preRotatePercent = rotationPercent
    }
    val needleRotation = animateFloatAsState(
        targetValue = if (isAnimation)
            Dimens.Rotation.MusicPageNeedleFinal
        else Dimens.Rotation.MusicPageNeedleInitial,
        animationSpec = tween(Constants.MusicPageValues.NeedleRotationDuration)
    )
    var posterGradianSize by remember { mutableStateOf(Size.Zero) }
    var needleConnecterSize by remember { mutableStateOf(Size.Zero) }
    var mainContainerSize by remember {mutableStateOf(Size.Zero)}
    Box(
        modifier = modifier
            .layoutId(Constants.MusicPageValues.MusicRotateDiskId)
            .aspectRatio(1f)
            .onGloballyPositioned { layoutCoordinates ->
                mainContainerSize = layoutCoordinates.size.toSize()
            },
        contentAlignment = Alignment.Center
    ) {

        if (mainContainerSize.width > 0 && mainContainerSize.height > 0){
            Box(
                modifier = Modifier
                    .fillMaxSize(Dimens.Size.MusicPageDiskSizeRatio)
                    .graphicsLayer {
                        rotationZ = if (onMusicChange) rotationPercent * 360f else animatedRotation
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = poster.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(Constants.MusicPageValues.MusicPosterToDiskRatio)
                        .clip(CircleShape)
                        .onGloballyPositioned { layoutCoordinates ->
                            posterGradianSize = layoutCoordinates.size.toSize()
                        }
                        .then(
                            if (posterGradianSize != Size.Zero) {
                                Modifier.drawWithContent {
                                    drawContent()
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                colorTheme.LightShadow.copy(alpha = 0.5f)
                                            ),
                                            center = Offset(size.width / 2, size.height / 2),
                                            radius = size.minDimension / 2
                                        )
                                    )
                                }
                            } else Modifier
                        )
                )
                Image(
                    painter = painterResource(id = discImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(Dimens.Alpha.MusicPageDiskPoster)
                        .shadow(
                            elevation = Dimens.Elevation.MusicPageDiskPoster,
                            shape = CircleShape,
                            clip = false,
                        )
                )
            }
            Box(modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    needleConnecterSize = layoutCoordinates.size.toSize()
                }
                .align(Alignment.TopEnd)
                .fillMaxHeight(Dimens.Size.MusicPageNeedleHeightRatio)
                .aspectRatio(Dimens.AspectRatio.MusicPageNeedle)
                .rotate(needleRotation.value)
                .scale(1.3f)
                .offset(
                    x = Dimens.Offset.MusicPageNeedleXOffset,
                    y = Dimens.Offset.MusicPageNeedleYOffset
                ),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = needleImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
@Composable
fun SongLyricSlider(
) {

}
@Composable
fun IconShadowed(
    modifier: Modifier,
    iconPainter: Int,
    colorTheme: ColorTheme,
    shape: Shape = RoundedCornerShape(Dimens.CornerRadius.MusicPageShadowedIconDefault),
    aspectRatio: Float = 1f,
    backgroundColor: Color = colorTheme.Background.copy(alpha = Dimens.Alpha.MusicPageShadowedIconBackground),
    tint: Color = colorTheme.Tint,
    padding: Dp = Dimens.Padding.MusicPageShadowedIconDefault,
    layoutId: String = "" ,//********************,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .layoutId(layoutId)
            .fillMaxHeight()
            .clip(shape)
            .border(
                width = Dimens.Size.MusicPageShadowedIconDarkBorder,
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, colorTheme.DarkShadow)
                ),
                shape = shape
            )
            .border(
                width = Dimens.Size.MusicPageShadowedIconLightBorder,
                brush = Brush.linearGradient(
                    colors = listOf(colorTheme.LightShadow, Color.Transparent, Color.Transparent)
                ),
                shape = shape
            )
            .aspectRatio(aspectRatio)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(id = iconPainter),
            contentDescription = null,
            tint = tint,
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}
