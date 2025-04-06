package com.soroush.eskandarie.musicplayer.presentation.ui.page.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.soroush.eskandarie.musicplayer.domain.usecase.SavingDeviceMusicToLocalDBUseCase
import com.soroush.eskandarie.musicplayer.framework.service.MusicPlaybackService
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.HomeActivity
import com.soroush.eskandarie.musicplayer.presentation.ui.page.splash.screen.SplashScreen
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
    @Inject lateinit var saveDeviceMusicToLocalDB : SavingDeviceMusicToLocalDBUseCase
    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startActivityWithDelay()
        CoroutineScope(Dispatchers.IO).launch {
            saveDeviceMusicToLocalDB()
        }
        setContent {
            MusicPlayerTheme {
                SplashScreen()
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
    //endregion
    private fun startActivityWithDelay(){
        val intent = Intent(this, HomeActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch{
            delay(Constants.SplashScreen.SplashDuration)
            startActivity(intent)
            finish()
        }
    }

}
