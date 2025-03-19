package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.data.local.MusicEntity
import com.soroush.eskandarie.musicplayer.data.repository.DeviceMusicRepositoryImpl
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.PlaybackState
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDeviceUsecase
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.RefreshQueueUseCase
import com.soroush.eskandarie.musicplayer.framework.service.MusicPlaybackService
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.music.MusicPage
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.viewmodel.HomeViewModel
import com.soroush.eskandarie.musicplayer.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity  : ComponentActivity() {
    private val viewmodel: HomeViewModel by viewModels()
    @Inject lateinit var refreshQueueUseCase: RefreshQueueUseCase
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_AUDIO
    )

    @OptIn(UnstableApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rep = DeviceMusicRepositoryImpl(this)
        val usecase = GetAllMusicFromDeviceUsecase(rep)
        checkPermissions()
        setContent {
            val scope = rememberCoroutineScope()
            var musicList by remember { mutableStateOf<List<MusicFile>>(emptyList()) }
            LaunchedEffect(Unit) {
                scope.launch {
                    usecase().collect{ music ->
                        musicList = music
                        val musicEntityList: MutableList<MusicEntity> = mutableListOf()
                        musicList.forEachIndexed{i, musicFile ->
                        val add = musicEntityList.add(MusicEntity(i.toLong(), musicFile.path))
                        }

                        Log.e("sth is wrong", "2 : ${musicEntityList.size}")
                        refreshQueueUseCase(musicEntityList)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {

//                    SearchField(
//                        setState = viewmodel::getHomeSetAction,
//                        getState = viewmodel.homeState.map { homeState ->
//                            homeState.searchFieldState.searchText
//                        }.collectAsState(initial = "")
//                    ) {
//
//                    }

                    HomePage(
                        modifier = Modifier
                            .statusBarsPadding()
                            .align(Alignment.TopCenter)
                            .padding(horizontal = (Dimens.Padding.HomeActivity))
                            .padding(bottom = 68.dp),
                        playlists = getPlaylist()
                    )

                    MusicPage(
                        modifier = Modifier
                    ){playbackState ->
                        if( playbackState == PlaybackState.PAUSED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startService(
                                    Intent(
                                        this@HomeActivity,
                                        MusicPlaybackService::class.java
                                    ).apply {
                                        action = Constants.PlaybackAction.Pause
                                    })
                            }
                        }else {
                            startService(
                                Intent(
                                    this@HomeActivity,
                                    MusicPlaybackService::class.java
                                ).apply {
                                    action = Constants.PlaybackAction.Resume
                                })
                        }
                    }

//                MusicPage(scrollState = scrollState)

            }
        }
    }

    //region Init Methods

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
    //endregion
    //region Normal Methods
    private fun getPlaylist(): List<Playlist> = listOf(
        Playlist(
            0,
            "Favorite",
            Uri.parse("android.resource://${this.packageName}/" + R.drawable.favorite_playlist)
        ),
        Playlist(
            1,
            "Playlist 1",
            Uri.parse("android.resource://${this.packageName}/" + R.drawable.shaj)
        ),
        Playlist(
            2,
            "Playlist 2",
            Uri.parse("android.resource://${this.packageName}/" + R.drawable.ghader)
        ),
        Playlist(
            3,
            "Playlist 3",
            Uri.parse("android.resource://${this.packageName}/" + R.drawable.simint)
        ),
        Playlist(
            4,
            "Playlist 4",
            Uri.parse("android.resource://${this.packageName}/" + R.drawable.sharhram)
        ),
        Playlist(
            5,
            "Playlist 5",
            Uri.parse("android.resource://${this.packageName}/" + R.drawable.empty_album)
        )
    )
    //endregion
}