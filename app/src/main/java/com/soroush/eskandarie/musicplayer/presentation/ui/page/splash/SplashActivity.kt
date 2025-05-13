package com.soroush.eskandarie.musicplayer.presentation.ui.page.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.soroush.eskandarie.musicplayer.framework.receiver.LockScreenReceiver
import com.soroush.eskandarie.musicplayer.framework.service.MusicPlaybackService
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.HomeActivity
import com.soroush.eskandarie.musicplayer.presentation.ui.page.splash.screen.SplashScreen
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme
import com.soroush.eskandarie.musicplayer.presentation.viewmodel.SplashViewModel
import com.soroush.eskandarie.musicplayer.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_AUDIO
    )

    //region Lifecycle Methods
    @kotlin.OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        if (PERMISSIONS.any { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            viewModel.firstTimeLaunchActions()
        }
        enableEdgeToEdge()
        startActivityWithDelay()
        setContent {
            val readExternalStoragePermissionState = rememberPermissionState(
                permission = Manifest.permission.READ_EXTERNAL_STORAGE
            )
            LaunchedEffect(
                key1 = readExternalStoragePermissionState.status
            ) {
                if(
                    readExternalStoragePermissionState.status.isGranted
                ){
                    viewModel.firstTimeLaunchActions()
                }
            }
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
        lifecycleScope.launch{
            delay(Constants.SplashScreen.SplashDuration)
            startActivity(intent)
            finish()
        }
    }
    private fun checkPermissions() {
        if (PERMISSIONS.any { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }
}
