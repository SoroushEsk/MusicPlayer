package com.soroush.eskandarie.musicplayer.presentation.ui.page.music

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.compose.ui.platform.LocalContext
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
import com.soroush.eskandarie.musicplayer.presentation.ui.MusicPageScrollState
import com.soroush.eskandarie.musicplayer.presentation.ui.animation.musicPageMotionLayoutConfig
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.util.Constants
import java.util.concurrent.Flow

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MusicPage(
    modifier: Modifier = Modifier,
    songPercent: State<Float>,
    colorTheme: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    posterShape: Shape = RoundedCornerShape(Dimens.CornerRadius.General),
    onClick: (playbackState: PlaybackState) -> Unit
) {

    val configuration = LocalConfiguration.current
    val resources = LocalContext.current.resources
    var progress by rememberSaveable { mutableStateOf(0.0f) }
    var scrollStatus by remember { mutableStateOf(MusicPageScrollState.NoScroll) }
    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.shaj)
    val palette = Palette.from(bitmap).generate()

    var posterCoordinates by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var mainContainerCoordinates by remember {
        mutableStateOf(Offset(0f, 0f))
    }
//    var songPercentState by remember { mutableStateOf(0f) }


    val dominantColor1 = Color(palette.getDominantColor(0)).copy(alpha = Dimens.Alpha.DomainColor1Alpha)
    val dominantColor2 = Color(palette.getDominantColor(0)).copy(alpha = Dimens.Alpha.DomainColor2Alpha)
    val vibrantColor1 = Color(palette.getVibrantColor(0)).copy(alpha = Dimens.Alpha.VibrantColor1Alpha)
    val vibrantColor2 = Color(palette.getVibrantColor(0)).copy(alpha = Dimens.Alpha.VibrantColor2Alpha)
    val mutedColor = Color(palette.getMutedColor(0)).copy(alpha = Dimens.Alpha.MutedColorAlpha)
    val listOfColors =
        listOf(dominantColor1, dominantColor2, vibrantColor1, vibrantColor2, mutedColor)
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
    var fingerY by remember { mutableStateOf(0f) }
    var isTouching by remember { mutableStateOf(false) }
    val maxScrollRange = configuration.screenHeightDp * LocalDensity.current.density

    val animatedProgress by animateFloatAsState(
        targetValue = when( scrollStatus ) {
            MusicPageScrollState.ScrollUp -> 1f
            MusicPageScrollState.ScrollDown -> 0f
            else -> progress
//            else -> {
//                if (isTouching) {
//                    Math.abs(fingerY - maxScrollRange) / maxScrollRange
//                } else {
//                    if (progress > 0.5f) {
//                        1f
//                    } else 0f
//                }
//            }
        },
        animationSpec = tween(
            durationMillis = if (isTouching) 0
                            else Constants.MusicPageValues.LeaveScrollDuration
        )
    )
    progress = animatedProgress

    var textWidth by remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition()
    val offsetX = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -textWidth,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 8000,
                easing = LinearEasing
            ), repeatMode = RepeatMode.Restart
        ), label = ""
    )
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

    var playbackState by remember{
        mutableStateOf(PlaybackState.PAUSED)
    }
    MotionLayout(
        motionScene = MotionScene(content = musicPageMotionLayoutConfig),
        progress = animatedProgress,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()

    ) {
        Column(
            modifier = modifier
                .layoutId(Constants.MusicBarValues.MotionLayoutContainerId)
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
                        if (delta < -5) scrollStatus = MusicPageScrollState.ScrollDown
                        else if (delta > 0) scrollStatus = MusicPageScrollState.ScrollUp
                        delta
                    }
                )
                .onGloballyPositioned { coordinates ->
                    mainContainerCoordinates =
                        Offset(coordinates.positionInWindow().x, coordinates.positionInWindow().y)
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.first().position
//                            if ( progress < 1f && scrollStatus == MusicPageScrollState.NoScroll)
//                                isTouching = event.changes.any { it.pressed }
//                            else isTouching = false
                            if (isTouching) {
                                fingerY = position.y + mainContainerCoordinates.y
                            }
                        }
                    }
                }
            ) {}
        Column(
            modifier = Modifier
                .layoutId(Constants.MusicBarValues.MotionLayoutTextContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = if(progress == 0f) Alignment.Start else Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tasnife Saze Khamoosh I have many more to say",
                modifier = modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutTitleId),
                color = colorTheme.Text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                onTextLayout = { textLayoutResult ->
                    textWidth = textLayoutResult.size.width.toFloat()
                }
            )
            Text(
                text = "Mohammadreza Shajarian",
                modifier = modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutArtistId),
                color = colorTheme.Text.copy(alpha = 0.70f),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
        IconsAtEndsRow(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.DownOptionIconContainerId),
            rightIcon =  R.drawable.options_list,
            leftIcon  =  R.drawable.down_arrow,
            colorTheme = colorTheme
        )
        Image(
            painter = painterResource(id = R.drawable.shaj),
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
            poster = R.drawable.shaj,
            discImage = if(isSystemInDarkTheme())R.drawable.gramaphone_disc else R.drawable.disk_light,
            needleImage = if(isSystemInDarkTheme()) R.drawable.needle else R.drawable.needle_light ,
            isAnimation = progress == 1f,
            resetRotation = progress == 0f,
            colorTheme = colorTheme
        )
        IconsAtEndsRow(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.AboveProgressBarContainerId),
            rightIcon =  R.drawable.filled_heart,
            leftIcon  =  R.drawable.playlist,
            colorTheme = colorTheme
        )
        ProgressBar(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.PlayProgressBarContainerId)
                .fillMaxWidth()
                .fillMaxHeight(0.05f),
            colorTheme = colorTheme,
            currentPosition = "01:31",
            totalDuration = "03:13",
            songPercent = songPercent.value
        )
        PLayControlShadow(
            modifier = modifier.rotate(90f),
            tint = colorTheme.Background,
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 5.dp + (progress * 7.dp.value).dp,
            layoutId = Constants.MusicBarValues.MotionLayoutBackIconId
        )
        PLayControlShadow(
            modifier = Modifier,
            tint = colorTheme.Background,
            iconPainter = if (playbackState == PlaybackState.PLAYING) R.drawable.play_button else R.drawable.pause,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 10.dp + (progress * 10.dp.value).dp,
            layoutId = Constants.MusicBarValues.MotionLayoutPlayIconId
        ){
            onClick(playbackState)
            if(playbackState == PlaybackState.PLAYING) playbackState = PlaybackState.PAUSED
            else playbackState = PlaybackState.PLAYING
        }
        PLayControlShadow(
            modifier = modifier,
            tint = colorTheme.Background,
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 5.dp + (progress * 7.dp.value).dp,
            layoutId = Constants.MusicBarValues.MotionLayoutForwardIconId
        )
        IconsAtEndsRow(
            modifier  = Modifier
                .layoutId(Constants.MusicPageValues.ShuffleRepeatContainerId),
            rightIcon =  R.drawable.repeat_disable,
            leftIcon  =  R.drawable.shuffle,
            colorTheme = colorTheme
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
    currentPosition: String,
    totalDuration: String,
    songPercent: Float,
    padding: PaddingValues = PaddingValues(
        horizontal = Dimens.Padding.MusicPageProgressBarDefaultHorizontal,
        vertical = Dimens.Padding.MusicPageProgressBarDefaultVertical
    )
) {

    Row(
        modifier = modifier
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentPosition,
            color = colorTheme.Text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = Dimens.Padding.MusicPageProgressBarCanvasHorizontal)
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
            text = totalDuration,
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
    extraPadding: Dp = Dimens.Padding.MusicPageIconsAtEndRowDefault
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
        if (size.width > 30 && size.height > 30) {

            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f),
                iconPainter = leftIcon,
                colorTheme = colorTheme
            )
            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f),
                iconPainter = rightIcon,
                colorTheme = colorTheme
            )
        }
    }
}
@Composable
fun SongPoster(
    modifier: Modifier,
    poster: Int,
    discImage: Int,
    needleImage: Int,
    isAnimation: Boolean,
    resetRotation: Boolean,
    colorTheme: ColorTheme,
    rotationLength: Int = Constants.MusicPageValues.DiskRotationDuration
) {
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    val needleRotation = animateFloatAsState(
        targetValue = if (isAnimation)
            Dimens.Rotation.MusicPageNeedleFinal
        else Dimens.Rotation.MusicPageNeedleInitial,
        animationSpec = tween(Constants.MusicPageValues.NeedleRotationDuration)
    )
    // Actions when the it's full screen and compact
    LaunchedEffect(key1 = resetRotation) {
        if ( resetRotation ){
            rotation.snapTo(0f)
        }
    }
    LaunchedEffect(key1 = isAnimation) {
        if (isAnimation) {
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(rotationLength, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) {
                currentRotation = this.value
            }
        } else {
            rotation.stop()
            currentRotation = rotation.value
        }
    }


    var posterGradianSize by remember { mutableStateOf(Size.Zero) }
    var needleConnecterSize by remember { mutableStateOf(Size.Zero) }
    var mainContainerSize by remember {mutableStateOf(Size.Zero)}
    LaunchedEffect(key1 = rotation.value) {
        Log.e("Rotation", "${rotation.value} ${isAnimation} ${resetRotation} \n ${currentRotation}")
    }

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
                        rotationZ = rotation.value
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = poster),
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
    backgroundColor: Color = Color.Transparent,
    tint: Color = colorTheme.Tint,
    padding: Dp = Dimens.Padding.MusicPageShadowedIconDefault,
    layoutId: String = "" //********************
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
            .background(backgroundColor),
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
