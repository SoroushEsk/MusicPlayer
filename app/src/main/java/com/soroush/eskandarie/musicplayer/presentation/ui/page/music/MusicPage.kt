package com.soroush.eskandarie.musicplayer.presentation.ui.page.music

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.focus.focusModifier
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.util.Constaints
import okio.ByteString.Companion.readByteString
import kotlin.time.times

//@OptIn(ExperimentalMotionApi::class)
//@Composable
//fun MusicPage(
//    modifier: Modifier = Modifier,
//    scrollState: ScrollState
//) {
//    val context = LocalContext.current
//
//    val motionProgress = scrollState.value.toFloat() / scrollState.maxValue.toFloat().coerceAtLeast(1f)
//    val motionSceneContent = remember {
//        context.resources.openRawResource(R.raw.music_page_constraint_set).bufferedReader().use { it.readText()
//        }
//    }
//    val motionScene = MotionScene(content = motionSceneContent)
//    MotionLayout(
//        modifier = Modifier
//            .layoutId(Constaints.HomePageValues.MusicPageMotionLayoutId),
//        motionScene = motionScene,
//        progress = motionProgress
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.DarkGray)
//                .layoutId(Constaints.MusicBarValues.MotionLayoutBoxId)
//    )
//    }
//}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProfileHeader(
    modifier: Modifier
) {

    val configuration = LocalConfiguration.current
    var fingerY by remember { mutableStateOf(0f) }
    var isTouching by remember { mutableStateOf(false) }
    val maxScrollRange =  configuration.screenHeightDp * LocalDensity.current.density
    var progress by remember { mutableStateOf(0.7f) }
    var offsetY by remember { mutableStateOf(0f) }

    LaunchedEffect(
        key1 = fingerY
    ) {
        progress = Math.abs(fingerY - maxScrollRange) / maxScrollRange
        Log.e("Scroll Progress", "Progress: ${fingerY}")
    }

    val context = LocalContext.current
    val scene = remember {
        try {
            context.resources.openRawResource(R.raw.music_page_constraint_set).readBytes().decodeToString()
        } catch (e: Exception) {
            Log.e("ProfileHeader", "Error loading motion scene", e)
            "" // Provide a fallback empty string
        }
    }

    MotionLayout(
        motionScene = MotionScene(content = scene),
        progress = progress,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val profilePictureProperties = motionProperties(id = "profile_pic")
        val usernameProperties = motionProperties(id = "username")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId("box")
                .onGloballyPositioned { coordinates ->
                    offsetY = coordinates.positionInWindow().y
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.first().position
                            isTouching = event.changes.any { it.pressed }
                            if (isTouching) {
                                fingerY = position.y + offsetY
                            }
                        }
                    }
                }
        ){
            if( progress < 1f){
                Image(
                    painter = painterResource(id = R.drawable.ed),
                    contentDescription = "profile_pic",
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = profilePictureProperties.value.color("background"),
                            shape = CircleShape
                        )
                        .layoutId("profile_pic")
                )
                Text(
                    text = "Slim Shady",
                    fontSize = 24.sp,
                    color = usernameProperties.value.color("background"),
                    modifier = Modifier.layoutId("username")
                )
            } else MusicPage()
        }

    }
}

@Composable
fun MusicPage(modifier: Modifier = Modifier) {
    var isRotatePoster by remember { mutableStateOf(true) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkTheme.Background)
        ,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SongDetail(
            modifier = modifier,
            songName = "Harighe Sabs" ,
            songArtist = "Ebi"//,
//            downIcon = R.drawable.down_arrow ,
//            optionsIcon = R.drawable.options_list
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        SongLyricSlider()
        SongPoster(
            modifier = modifier,
            poster = R.drawable.shaj,
            discImage = R.drawable.gramaphone_disc,
            needleImage = R.drawable.needle,
            isAnimation = isRotatePoster
        )
        IconRowAboveProgressBar(modifier = modifier, R.drawable.filled_heart, R.drawable.playlist)
        ProgressBar(
            modifier = modifier,
            colorTheme = DarkTheme,
            currentPosition = "01:31",
            totalDuration = "03:13",
            songPercent = 0.73f
        )
        IconBelowProgressBar(
            modifier = modifier
        )
        Spacer(modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f))
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
        PLayControlShaow(
            modifier = modifier
                .rotate(90f)
                .weight(1.3f),
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Primary,
            padding = 18.dp
        )
        Spacer(modifier = Modifier
            .width(24.dp)
            .fillMaxHeight())
        PLayControlShaow(
            modifier = modifier
                .weight(1.75f),
            iconPainter = R.drawable.pause,
            backgroundColor = colorTheme.Primary,
            shape = CircleShape,
            padding = 26.dp
        )
        Spacer(modifier = Modifier
            .width(24.dp)
            .fillMaxHeight())
        PLayControlShaow(
            modifier = modifier
                .weight(1.3f),
            iconPainter = R.drawable.fast_forward,
            shape = CircleShape,
            backgroundColor = colorTheme.Primary,
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
fun PLayControlShaow(
    modifier: Modifier,
    iconPainter : Int,
    shape : Shape = RoundedCornerShape(16.dp),
    colorTheme: ColorTheme = DarkTheme,
    aspectRatio : Float = 1f,
    backgroundColor: Color = Color.Transparent,
    tint : Color = colorTheme.DarkSurface,
    padding: Dp = 10.dp
){
    Box(
        modifier = modifier
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
            .fillMaxHeight(0.13f)
            .padding(16.dp, 0.dp),
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
                .weight(1.5f),
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
    padding: Dp = 10.dp
){
    Box(
        modifier = modifier
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
