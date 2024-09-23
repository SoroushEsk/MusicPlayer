package com.soroush.musicapp

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.soroush.musicapp.page.music.ui.theme.Dark

class MusicPageActivity {
}

@Preview
@Composable
fun musicPagePreview() {
    MusicPage(Modifier)
}

@Composable
fun MusicPage(modifier: Modifier = Modifier) {
    var isRotatePoster by remember { mutableStateOf(true) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Dark.Background)
    ) {
        SongDetail(modifier = modifier, songName = "Harighe Sabs" , songArtist = "Ebi", downIcon = R.drawable.down_arrow , optionsIcon = R.drawable.options_list )
        Spacer(modifier = Modifier.height(4.dp))
        SongLyricSlider()
        SongPoster(modifier = modifier, poster = R.drawable.ebi, discImage = R.drawable.gramaphone_disc, needleImage = R.drawable.needle, isAnimation = isRotatePoster)

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
    var boxSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
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
                        boxSize = layoutCoordinates.size.toSize()
                    }
                    .then(
                        if (boxSize != Size.Zero) {
                            Modifier.drawWithContent {
                                drawContent()
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color.Transparent, Color.Black),
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

        Image(
            painter = painterResource(id = needleImage),
            contentDescription = null,
            modifier = modifier
                .align(Alignment.TopEnd)
                .fillMaxHeight(0.4f)
                .aspectRatio(0.6f)
                .rotate(-20f)
                .offset(-16.dp, 20.dp)
        )
    }
}
@Composable
fun SongLyricSlider(
    modifier: Modifier = Modifier
) {

}
@Composable
fun SongDetail(
    modifier: Modifier,
    songName: String,
    songArtist: String,
    downIcon: Int,
    optionsIcon: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
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
            modifier = modifier.weight(1.5f),
            iconPainter = R.drawable.options_list
        )
    }
}
@Composable
fun IconShadowed(
    modifier: Modifier,
    iconPainter : Int
){
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                spotColor = Dark.LightShadow
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                spotColor = Dark.LightShadow
            )
            .background(Dark.Background)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = painterResource(id = iconPainter),
            contentDescription = null,
            tint = Dark.DarkSurface,
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = false,
                    spotColor = Dark.DarkShadow
                )
                .padding(8.dp)
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
            color = Dark.Text)
        Spacer(modifier =  Modifier.height(4.dp))
        Text(text = artist,
            color = Dark.Text)
    }
    
}
