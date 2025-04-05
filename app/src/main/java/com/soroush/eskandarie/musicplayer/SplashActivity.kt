package com.soroush.eskandarie.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.media3.common.util.UnstableApi
import com.soroush.eskandarie.musicplayer.framework.service.MusicPlaybackService
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.HomeActivity
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme
import com.soroush.eskandarie.musicplayer.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    @Inject lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = Intent(this, HomeActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch{
            delay(Constants.SplashScreen.SplashDuration)
            startActivity(intent)
            finish()
        }
        setContent {
            val iconAnimation = rememberInfiniteTransition()
            val iconScale by iconAnimation.animateFloat(
                initialValue = 1f,
                targetValue = 1.03f,
                animationSpec = InfiniteRepeatableSpec(
                    animation = tween(400),
                    repeatMode = RepeatMode.Restart
                )
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

            MusicPlayerTheme {
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
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        startService(
            Intent(
                this@SplashActivity,
                MusicPlaybackService::class.java
            )
        )
    }
}
