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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.util.Constants

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MusicPage(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    posterShape: Shape = RoundedCornerShape(Dimens.CornerRadius.General)
) {

    val configuration = LocalConfiguration.current
    val resources = LocalContext.current.resources
    val context = LocalContext.current
    var progress by remember { mutableStateOf(0.4f) }

    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.shaj)
    val palette = Palette.from(bitmap).generate()

    var posterCoordinates by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var mainContainerCoordinates by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val dominantColor1 = Color(palette.getDominantColor(0)).copy(alpha = 0.90f)
    val dominantColor2 = Color(palette.getDominantColor(0)).copy(alpha = 0.570f)
    val vibrantColor1 = Color(palette.getVibrantColor(0)).copy(alpha = 0.50f)
    val vibrantColor2 = Color(palette.getVibrantColor(0)).copy(alpha = 0.30f)
    val mutedColor = Color(palette.getMutedColor(0)).copy(alpha = 0.10f)
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

    var isRotatePoster by remember { mutableStateOf(true) }
    var preFingerY by remember { mutableStateOf(0f) }
    var isScroll by remember { mutableStateOf(false) }
    var fingerY by remember { mutableStateOf(0f) }
    var isTouching by remember { mutableStateOf(false) }
    val maxScrollRange = configuration.screenHeightDp * LocalDensity.current.density

    val animatedProgress by animateFloatAsState(
        targetValue = if (isTouching) {
            Math.abs(fingerY - maxScrollRange) / maxScrollRange
        } else {
            if (progress > 0.5f) {
                if (isScroll) 0.99f else 1f
            } else 0f
        },
        animationSpec = tween(durationMillis = if (isTouching) 0 else 700)
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

    // TODO(" create rotate functionalty" )
    val scene = remember {
        try {
            context.resources.openRawResource(R.raw.music_page_constraint_set).readBytes()
                .decodeToString()
        } catch (e: Exception) {
            ""
        }
    }

    var musicBarPosterWidth by remember { mutableStateOf(1f) }
    var isTheSameSize by remember { mutableStateOf(false)}
    val pagePosterAlphaAnimation by animateFloatAsState(
        targetValue = if (isTheSameSize) 1f else 0f,
        animationSpec = tween(durationMillis = if( progress == 1f || progress == 0f ) 0 else 900)
    )
    val barPosterAlphaAnimation by animateFloatAsState(
        targetValue = if (isTheSameSize) 1f else 0f,
        animationSpec = tween(durationMillis = if( progress == 1f || progress == 0f ) 0 else 300)
    )
    MotionLayout(
        motionScene = MotionScene(content = scene),
        progress = progress,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .then(
                if (progress < 1f) Modifier.navigationBarsPadding()
                else Modifier.statusBarsPadding()
            )
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
                .background(radialGradientBrush)
                .onGloballyPositioned { coordinates ->
                    mainContainerCoordinates =
                        Offset(coordinates.positionInWindow().x, coordinates.positionInWindow().y)
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.first().position
                            isTouching = event.changes.any { it.pressed }
                            if (isTouching) {
                                fingerY = position.y + mainContainerCoordinates.y
                            }
                        }
                    }
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
            leftIcon  =  R.drawable.down_arrow
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
                    val offsetX = coordinates.positionInWindow().x.toFloat()
                    val offsetY = coordinates.positionInWindow().y.toFloat()
                    posterCoordinates = Offset(offsetX + width / 2, offsetY + height / 2)
                    Log.e("Width", "${(width * 0.8f * Constants.MusicPageValues.MusicPosterToDiskRatio)}")
                    if ((width * 0.8f * Constants.MusicPageValues.MusicPosterToDiskRatio) >= musicBarPosterWidth) {
                        isTheSameSize = false
                    }else isTheSameSize = true
                },
            poster = R.drawable.shaj,
            discImage = R.drawable.gramaphone_disc,
            needleImage = R.drawable.needle,
            isAnimation = progress == 1f,
            resetRotation = progress == 0f
        )

        IconsAtEndsRow(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.AboveProgressBarContainerId),
            rightIcon =  R.drawable.filled_heart,
            leftIcon  =  R.drawable.playlist
        )

        ProgressBar(
            modifier = Modifier
                .layoutId(Constants.MusicPageValues.PlayProgressBarContainerId)
                .fillMaxWidth()
                .fillMaxHeight(0.05f),
            colorTheme = colorTheme,
            currentPosition = "01:31",
            totalDuration = "03:13",
            songPercent = 0.73f
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
            iconPainter = R.drawable.pause,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 10.dp + (progress * 10.dp.value).dp,
            layoutId = Constants.MusicBarValues.MotionLayoutPlayIconId
        )
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
            leftIcon  =  R.drawable.shuffle
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
fun MusicPageFullScreen(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
    onDownIconClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorTheme.Background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {

        SongDetail(
            modifier = modifier,
            songName = "Harighe Sabs",
            songArtist = "Ebi",
            onDownIconClick = onDownIconClick
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        SongLyricSlider()
//        SongPoster(
//            modifier = modifier,
//            poster = R.drawable.shaj,
//            discImage = R.drawable.gramaphone_disc,
//            needleImage = R.drawable.needle,
//            isAnimation = isRotatePoster
//        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
//        IconRowAboveProgressBar(modifier = modifier, R.drawable.filled_heart, R.drawable.playlist)
//        ProgressBar(
//            modifier = modifier,
//            colorTheme = DarkTheme,
//            currentPosition = "01:31",
//            totalDuration = "03:13",
//            songPercent = 0.73f
//        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        IconBelowProgressBar(
            modifier = modifier
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(Dimens.Spacing.MusicPageSpaceBetween)
        )
    }
}

@Composable
fun IconBelowProgressBar(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme = DarkTheme
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.30f)
            .padding(16.dp, 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconShadowed(
            modifier = modifier
                .weight(1f),
            iconPainter = R.drawable.shuffle,
        )
        Spacer(
            modifier = Modifier
                .width(28.dp)
                .fillMaxHeight()
        )
        PLayControlShadow(
            modifier = modifier
                .rotate(90f)
                .weight(1.3f),
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 18.dp
        )
        Spacer(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight()
        )
        PLayControlShadow(
            modifier = modifier
                .weight(1.75f),
            iconPainter = R.drawable.pause,
            backgroundColor = colorTheme.Icon,
            shape = CircleShape,
            padding = 26.dp
        )
        Spacer(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight()
        )
        PLayControlShadow(
            modifier = modifier
                .weight(1.3f),
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 18.dp
        )
        Spacer(
            modifier = Modifier
                .width(28.dp)
                .fillMaxHeight()
        )
        IconShadowed(
            modifier = modifier
                .weight(1f),
            iconPainter = R.drawable.repeat_disable,
        )
    }
}

@Composable
fun PLayControlShadow(
    modifier: Modifier,
    iconPainter: Int,
    shape: Shape = RoundedCornerShape(16.dp),
    colorTheme: ColorTheme = DarkTheme,
    aspectRatio: Float = 1f,
    backgroundColor: Color = Color.Transparent,
    tint: Color = colorTheme.DarkSurface,
    padding: Dp = 10.dp,
    layoutId: String = ""
) {
    Box(
        modifier = modifier
            .layoutId(layoutId)
            .fillMaxHeight()
            .aspectRatio(aspectRatio)
            .fillMaxWidth()
            .clip(shape)
            .shadow(
                elevation = 4.dp,
                shape = shape,
                clip = true,
                spotColor = colorTheme.DarkShadow
            )
            .shadow(
                elevation = 2.dp,
                shape = shape,
                clip = true,
                spotColor = colorTheme.DarkShadow
            )
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

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
    currentPosition: String,
    totalDuration: String,
    songPercent: Float
) {

    Row(
        modifier = modifier
            .padding(16.dp, 0.dp),
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
                .padding(horizontal = 12.dp)
        ) {
            if (size.width > 0 && size.height > 0) {
                drawLine(
                    color = colorTheme.LightShadow,
                    start = Offset(0f, center.y),
                    end = Offset(size.width, center.y),
                    strokeWidth = 8.dp.toPx(),
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
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawPoints(
                    points = listOf(Offset(size.width * songPercent, center.y)),
                    pointMode = PointMode.Points,
                    color = colorTheme.Secondary,
                    strokeWidth = (8 * 1.8f).dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        Text(text = totalDuration, color = colorTheme.Text)
    }
}

@Composable
fun IconsAtEndsRow(
    modifier: Modifier = Modifier,
    rightIcon: Int,
    leftIcon: Int
) {
    var size by remember {
        mutableStateOf(Size.Zero)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onGloballyPositioned { coordinates ->
                size = coordinates.size.toSize()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (size.width > 30 && size.height > 30) {

            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f),
                iconPainter = leftIcon
            )
            IconShadowed(
                modifier = Modifier
                    .aspectRatio(1f),
                iconPainter = rightIcon
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
    rotationLength: Int = 15000,
    isAnimation: Boolean,
    resetRotation: Boolean
) {
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

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
                    .fillMaxSize(0.8f)
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
                                                DarkTheme.Surface
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
                        .shadow(
                            elevation = 10.dp,
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
                .fillMaxHeight(0.4f)
                .aspectRatio(0.6f)
                .rotate(-15f)
                .offset(-16.dp, 32.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    modifier = Modifier
                        .then(
                            if (needleConnecterSize != Size.Zero) {
                                Modifier.offset(
                                    y = -(needleConnecterSize.height * 18f / 211f).dp,
                                    x = (needleConnecterSize.width * 18f / 150f).dp
                                )
                            } else Modifier
                        )
                        .scale(0.4f)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            clip = false,
                            spotColor = DarkTheme.DarkSurface
                        )
                        .rotate(60f),
                    painter = painterResource(R.drawable.rec),
                    contentDescription = null
                )
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
fun SongDetail(
    modifier: Modifier,
    songName: String,
    onDownIconClick: () -> Unit,
    songArtist: String
) {
    Row(
        modifier = modifier
            .fillMaxHeight(0.13f)
            .fillMaxWidth()
            .padding(16.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconShadowed(
            modifier = modifier
                .weight(1.5f)
                .clickable {
                    onDownIconClick()
                },
            iconPainter = R.drawable.down_arrow
        )
        SongTitle(
            modifier = modifier.weight(10f),
            name = songName,
            artist = songArtist
        )
        IconShadowed(
            modifier = modifier
                .weight(1.5f),
            iconPainter = R.drawable.options_list
        )
    }
}

@Composable
fun IconShadowed(
    modifier: Modifier,
    iconPainter: Int,
    shape: Shape = RoundedCornerShape(12.dp),
    colorTheme: ColorTheme = if (isSystemInDarkTheme())  DarkTheme else LightTheme,
    aspectRatio: Float = 1f,
    backgroundColor: Color = Color.Transparent,
    tint: Color = colorTheme.Tint,
    padding: Dp = 10.dp,
    layoutId: String = "" //********************
) {
    Box(
        modifier = modifier
            .layoutId(layoutId)
            .fillMaxHeight()
            .clip(shape)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, colorTheme.DarkShadow)
                ),
                shape = shape
            )
            .border(
                width = 2.dp,
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

@Composable
fun SongTitle(
    modifier: Modifier = Modifier,
    name: String,
    artist: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            color = DarkTheme.Text
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artist,
            color = DarkTheme.Text
        )
    }

}
