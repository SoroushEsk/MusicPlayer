package com.soroush.eskandarie.musicplayer.presentation.nav

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.PlaylistPage
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens

@Composable
fun HomeActivityNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    getLazyListState: @Composable () -> State<LazyListState>,
    getPlaylist: State<List<Playlist>>,
    setLazyState: (playlistName: String)->Unit,
    getAllMusic: () -> List<MusicFile>
) {
    val startDestination: String = Destination.HomeScreen.route
    val localContext = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Destination.HomeScreen.route,
        modifier = modifier
    ) {
        composable(route = Destination.HomeScreen.route) {
            HomePage(
                modifier = Modifier
                    .padding(horizontal = (Dimens.Padding.HomeActivity)),
                loadPlaylist = getPlaylist,
                navController = navController
            )
        }
        composable(route = Destination.AllMusicScreen.route) {
            LaunchedEffect(Unit) {
                setLazyState(Destination.AllMusicScreen.route)
            }
            PlaylistPage(
                lazyListState = getLazyListState().value,
                loadMoreItems = { _, _1 ->
                    getAllMusic().shuffled()
                }
            )
        }
    }
}




