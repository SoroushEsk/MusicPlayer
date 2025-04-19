package com.soroush.eskandarie.musicplayer.presentation.nav

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.PlaylistPage
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import kotlinx.coroutines.flow.StateFlow

@Composable
fun  HomeActivityNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    getState: (action: HomeViewModelGetStateAction) -> StateFlow<*>,
    setState: (action: HomeViewModelSetStateAction) -> Unit,
    navControllerAction: ()->Unit,
    musicLazyPaging: LazyPagingItems<MusicFile>
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
                loadPlaylist = (getState(HomeViewModelGetStateAction.GetPlaylists)as StateFlow<List<Playlist>>).collectAsState() ,
                navController = navController
            )
        }
        composable(route = Destination.AllMusicScreen.route) {
            val lazyListState by getState(HomeViewModelGetStateAction.GetLazyListState).collectAsState()
            PlaylistPage(
                lazyListState = lazyListState as LazyListState,
                loadMoreItems = { _, _1 ->
                    //(getState(HomeViewModelGetStateAction.GetMusicFiles).value as List<MusicFile>).shuffled()
                    emptyList()
                },
                pageDataItem = musicLazyPaging
            )
        }
    }
}




