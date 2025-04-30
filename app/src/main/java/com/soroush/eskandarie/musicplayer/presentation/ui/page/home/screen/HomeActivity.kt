package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.hardware.lights.Light
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.session.legacy.MediaControllerCompat
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.common.util.concurrent.ListenableFuture
import com.soroush.eskandarie.musicplayer.domain.usecase.queue.RefreshQueueUseCase
import com.soroush.eskandarie.musicplayer.framework.service.MusicPlaybackService
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.nav.HomeActivityNavHost
import com.soroush.eskandarie.musicplayer.presentation.ui.page.common.SearchField
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.viewmodel.HomeViewModel
import com.soroush.eskandarie.musicplayer.shared_component.media_controller.MediaControllerObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    //region Fields
    private val viewmodel: HomeViewModel by viewModels()

    @Inject
    lateinit var refreshQueueUseCase: RefreshQueueUseCase
    lateinit var mediaFuture: ListenableFuture<MediaController>

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressUpdateJob: Job? = null
    private lateinit var mediaControllerObserver: MediaControllerObserver
    var songPercentage: Float = 0f
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_AUDIO
    )

    //endregion
    @OptIn(UnstableApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewmodel.viewModelSetAction(HomeViewModelSetStateAction.GetAllMusicFiles)
        viewmodel.viewModelSetAction(HomeViewModelSetStateAction.GetAllPlaylists)
        //checkPermissions()
        setContent {
            val navController = rememberNavController()
            LaunchedEffect(Unit) {

                while (true) {
                    viewmodel.viewModelSetAction(HomeViewModelSetStateAction.SetMusicPercent)
                    delay(1000)
                }
            }
            val scrollDirection = remember { mutableStateOf("none") }

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        if (available.y > 0) {
                            scrollDirection.value = "up"
                        } else if (available.y < 0) {
                            scrollDirection.value = "down"
                        }
                        return Offset.Zero
                    }
                }
            }
            val colorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorTheme.Background)
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .nestedScroll(nestedScrollConnection)
                ) {
//                    Log.e("12345", "${isTopBarVisible.value} $isAtStart $isScrollingUp")
//                    if (scrollDirection.value == "up") {
//                        SearchField(
//                            modifier = Modifier
//                                .padding(horizontal = Dimens.Padding.HomeActivity),
//                            setState = viewmodel::viewModelSetAction,
//                            getState = viewmodel.homeState
//                                .map { it.searchFieldState.searchText }
//                                .collectAsState(initial = "")
//                        ) {}
//
//
//                    }
                    HomeActivityNavHost(
                        navController = navController,
                        modifier = Modifier,
                        getState = viewmodel::viewModelGetStateActions,
                        setState = viewmodel::viewModelSetAction,
                        musicLazyPaging = viewmodel::getMusicPageList
                    )
                }
                MusicPage(
                    modifier = Modifier,
                    setState = viewmodel::viewModelSetAction,
                    getState = viewmodel::viewModelGetStateActions
                ) { playbackState ->

                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        initialMediaController()
        viewmodel.viewModelSetAction(HomeViewModelSetStateAction.FillFolderRequirements)

    }
    //region Init Methods
    @OptIn(UnstableApi::class)
    private fun initialMediaController() {
        val sessionToken =
            SessionToken(
                this@HomeActivity,
                ComponentName(this@HomeActivity, MusicPlaybackService::class.java)
            )
        mediaControllerObserver =
            MediaControllerObserver(
                this@HomeActivity,
                sessionToken,
                lifecycle,
                viewmodel::viewModelSetAction
            )

    }

    private fun checkPermissions() {
        if (PERMISSIONS.any {
                ContextCompat.checkSelfPermission(
                    this,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }
    //endregion
    //region Normal Methods

    //endregion
}