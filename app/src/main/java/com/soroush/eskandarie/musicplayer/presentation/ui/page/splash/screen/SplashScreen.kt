package com.soroush.eskandarie.musicplayer.presentation.ui.page.splash.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.util.Constants

@Composable
fun SplashScreen(){
    val iconAnimation = rememberInfiniteTransition(label = "")
    val iconScale by iconAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(400),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    val alphaAnimation = remember {
        Animatable(0f)
    }
    LaunchedEffect (Unit){
        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(Constants.SplashScreen.SplashDuration.toInt())
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alphaAnimation.value),
        color = if(isSystemInDarkTheme()) DarkTheme.Surface else LightTheme.Surface
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.scale(iconScale)
        ){
            Image(
                modifier = Modifier
                    .fillMaxSize(Dimens.Size.SplashScreenIcon)
                    .aspectRatio(Dimens.AspectRatio.SplashIcon),
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = Constants.SplashScreen.SplashScreenIconDescription
            )
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .padding(
                    horizontal = Dimens.Padding.Zero,
                    vertical = Dimens.Padding.SplashScreenTextDown
                )

        ){
            Text(
                color = if(isSystemInDarkTheme()) DarkTheme.Text else LightTheme.Text,
                text = Constants.SplashScreen.SplashScreenAppName,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}