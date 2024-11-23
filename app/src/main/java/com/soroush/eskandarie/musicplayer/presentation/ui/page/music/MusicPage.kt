package com.soroush.eskandarie.musicplayer.presentation.ui.page.music

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
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
    colorTheme: ColorTheme = if( isSystemInDarkTheme()) DarkTheme else LightTheme,
    posterShape: Shape = RoundedCornerShape(Dimens.CornerRadius.General)
) {

    val configuration = LocalConfiguration.current
    val resources = LocalContext.current.resources

    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.shaj)
    val palette = Palette.from(bitmap).generate()
//    val dominantSwatch = palette.dominantSwatch
//    val vibrantSwatch = palette.vibrantSwatch
//    val mutedSwatch = palette.mutedSwatch
//
//    val dominantColor = dominantSwatch?.rgb ?: 0
//    val vibrantColor = vibrantSwatch?.rgb ?: 0
//    val mutedColor = mutedSwatch?.rgb ?: 0
//
//    Log.e("Dominant Color"," $dominantColor")
//    Log.e("Vibrant Color"," $vibrantColor")
//    Log.e("Muted Color", " $mutedColor")
//


    val dominantColor = Color(palette.getDominantColor(0)).copy(alpha = 0.75f)
    val vibrantColor = Color(palette.getVibrantColor(0)).copy(alpha = 0.5f)
    val mutedColor = Color(palette.getMutedColor(0)).copy(alpha = 0.4f)
    
    val radialGradientBrush = Brush.linearGradient(
        colors = listOf( dominantColor, vibrantColor , mutedColor),
        start = Offset(0f, 0f),
        end = Offset(configuration.screenWidthDp.toFloat()* LocalDensity.current.density, 0f)
    )
    var progress by remember { mutableStateOf(0f) }

    var preFingerY by remember{mutableStateOf(0f)}
    var isScroll by remember { mutableStateOf(false) }
    var fingerY by remember { mutableStateOf(0f) }
    var isTouching by remember { mutableStateOf(false) }
    val maxScrollRange =  configuration.screenHeightDp * LocalDensity.current.density
    var offsetY by remember { mutableStateOf(0f) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isTouching) {
            Math.abs(fingerY - maxScrollRange) / maxScrollRange
        } else {
            if (progress > 0.5f) {
                if (isScroll) 0.99f else 1f
            } else 0f
        },
        animationSpec = tween(durationMillis = if ( isTouching ) 0 else 700)
    )

    progress = animatedProgress

    Log.e("ProfileHeader", "Error loading motion scene :$progress ")

    val context = LocalContext.current
    val scene = remember {
        try {
            context.resources.openRawResource(R.raw.music_page_constraint_set).readBytes().decodeToString()
        } catch (e: Exception) {
            ""
        }
    }
//    if ( progress < 1f)
        MotionLayout(
            motionScene = MotionScene(content = scene),
            progress = progress,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Row (
                modifier = modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutContainerId)
                    .clip(posterShape)
                    .background(colorTheme.Background)
                    .background(radialGradientBrush)
            ){

            }
            Image(
                painter = painterResource(id = R.drawable.shaj),
                contentDescription = Constants.MusicPageValues.MusicPosterDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutPosterId)
                    .padding(Dimens.Padding.MusicBarMusicPoster)
                    .aspectRatio(1f)
                    .clip(posterShape)
            )

            Text(
                text = "Tasnife Saze Khamoosh",
                modifier = modifier.layoutId(Constants.MusicBarValues.MotionLayoutTitleId),
                color = colorTheme.Text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Mohammadreza Shajarian",
                modifier = modifier.layoutId(Constants.MusicBarValues.MotionLayoutArtistId),
                color = colorTheme.Text.copy(alpha = 0.55f),
                style = MaterialTheme.typography.bodyMedium
            )
            Box(
                modifier = Modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutTextContainer)
            )
            PLayControlShadow(
                modifier = modifier.rotate(90f),
                tint = colorTheme.Background,
                iconPainter = R.drawable.fast_forward,
                shape = CircleShape,
                backgroundColor = colorTheme.Icon,
                padding = 6.dp,
                layoutId = Constants.MusicBarValues.MotionLayoutBackIconId
            )
            PLayControlShadow(
                modifier = modifier,
                tint = colorTheme.Background,
                iconPainter = R.drawable.pause,
                shape = CircleShape,
                backgroundColor = colorTheme.Icon,
                padding = 10.dp,
                layoutId = Constants.MusicBarValues.MotionLayoutPlayIconId
            )
            PLayControlShadow(
                modifier = modifier,
                tint = colorTheme.Background,
                iconPainter = R.drawable.fast_forward,
                shape = CircleShape,
                backgroundColor = colorTheme.Icon,
                padding = 6.dp,
                layoutId = Constants.MusicBarValues.MotionLayoutForwardIconId
            )

            Image(
                painter = painterResource(id = R.drawable.playlist),
                contentDescription = Constants.MusicPageValues.MusicPosterDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .layoutId(Constants.MusicBarValues.MotionLayoutPlaylistIconId)
                    .padding(6.dp)
                    .aspectRatio(1f)
            )

        }
//    else
//        MusicPageFullScreen(colorTheme = colorTheme, modifier = Modifier. pointerInput(Unit) {
//            awaitPointerEventScope {
//                while (true) {
//                    val event = awaitPointerEvent()
//                    val position = event.changes.first().position
//                    fingerY = position.y
//                    }
//                }
//            }
//        ){}

}

@Composable
fun MusicPageFullScreen(
    modifier: Modifier = Modifier,
    colorTheme: ColorTheme,
    onDownIconClick: () -> Unit
) {
    var isRotatePoster by remember { mutableStateOf(true) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorTheme.Background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {

        SongDetail(
            modifier = modifier,
            songName = "Harighe Sabs" ,
            songArtist = "Ebi",
            onDownIconClick = onDownIconClick
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(1f))
        SongLyricSlider()
        SongPoster(
            modifier = modifier,
            poster = R.drawable.shaj,
            discImage = R.drawable.gramaphone_disc,
            needleImage = R.drawable.needle,
            isAnimation = isRotatePoster
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(1f))
        IconRowAboveProgressBar(modifier = modifier, R.drawable.filled_heart, R.drawable.playlist)
        ProgressBar(
            modifier = modifier,
            colorTheme = DarkTheme,
            currentPosition = "01:31",
            totalDuration = "03:13",
            songPercent = 0.73f
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(1f))
        IconBelowProgressBar(
            modifier = modifier
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(3f))
        Spacer(modifier = modifier
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
    ){
        IconShadowed(
            modifier = modifier
                .weight(1f),
            iconPainter = R.drawable.shuffle,
        )
        Spacer(modifier = Modifier
            .width(28.dp)
            .fillMaxHeight())
        PLayControlShadow(
            modifier = modifier
                .rotate(90f)
                .weight(1.3f),
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 18.dp
        )
        Spacer(modifier = Modifier
            .width(24.dp)
            .fillMaxHeight())
        PLayControlShadow(
            modifier = modifier
                .weight(1.75f),
            iconPainter = R.drawable.pause,
            backgroundColor = colorTheme.Icon,
            shape = CircleShape,
            padding = 26.dp
        )
        Spacer(modifier = Modifier
            .width(24.dp)
            .fillMaxHeight())
        PLayControlShadow(
            modifier = modifier
                .weight(1.3f),
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Icon,
            padding = 18.dp
        )
        Spacer(modifier = Modifier
            .width(28.dp)
            .fillMaxHeight())
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
    iconPainter : Int,
    shape : Shape = RoundedCornerShape(16.dp),
    colorTheme: ColorTheme = DarkTheme,
    aspectRatio : Float = 1f,
    backgroundColor: Color = Color.Transparent,
    tint : Color = colorTheme.DarkSurface,
    padding: Dp = 10.dp,
    layoutId: String = ""
){
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
    ){
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
            .fillMaxWidth()
            .fillMaxHeight(0.13f)
            .padding(16.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = currentPosition, color = colorTheme.Text)
        Canvas(modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(12.dp, 0.dp)){
            drawLine(
                color = colorTheme.DarkShadow,
                start = Offset(0f, center.y),
                end   = Offset(size.width, center.y),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(colorTheme.Primary, colorTheme.Secondary),
                    start = Offset(center.x, center.y),
                    end = Offset(size.width * songPercent, center.y)
                ),
                start = Offset(0f, center.y),
                end   = Offset(size.width * songPercent, center.y),
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
        Text(text = totalDuration, color = colorTheme.Text)
    }
}
@Composable
fun IconRowAboveProgressBar(
    modifier: Modifier = Modifier,
    heartImage: Int,
    playlistImage: Int
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.Size.MusicPageAboveProgressBarHeight)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconShadowed(modifier = modifier,
            iconPainter = playlistImage
        )
        IconShadowed(modifier = modifier,
            iconPainter = heartImage
        )
    }
}
@Composable
fun SongPoster(
    modifier        : Modifier,
    poster          : Int,
    discImage       : Int,
    needleImage     : Int,
    rotationLength  : Int = 20000,
    isAnimation     : Boolean
){
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isAnimation) {
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .aspectRatio(1f)
        ,
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = modifier
                .fillMaxSize(0.8f)
                .graphicsLayer {
                    rotationZ = rotation.value
                },
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = poster),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxSize(142 / 255f)
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
                modifier = modifier
                    .fillMaxSize()
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        clip = false,
                    )
            )
        }
        Box(modifier = modifier
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
                modifier = modifier
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
    iconPainter : Int,
    shape : Shape = RoundedCornerShape(16.dp),
    colorTheme: ColorTheme = DarkTheme,
    aspectRatio : Float = 1f,
    backgroundColor: Color = Color.Transparent,
    tint : Color = colorTheme.DarkSurface,
    padding: Dp = 10.dp,
    layoutId: String = "" //********************
){
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
    ){
        Icon(
            painter = painterResource(id = iconPainter),
            contentDescription = null,
            tint = tint,
            modifier = modifier
                .fillMaxSize()
                .shadow(
                    elevation = 2.dp,
                    shape = shape,
                    clip = true,
                    spotColor = colorTheme.LightShadow
                )
                .padding(padding)
        )
    }
}
@Composable
fun SongTitle(modifier: Modifier = Modifier,
              name    : String,
              artist  : String
) {
    Column(modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = name,
            color = DarkTheme.Text)
        Spacer(modifier =  Modifier.height(4.dp))
        Text(text = artist,
            color = DarkTheme.Text)
    }

}
