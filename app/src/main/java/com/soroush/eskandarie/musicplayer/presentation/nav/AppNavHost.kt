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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.FolderPage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.PlaylistPage
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@JvmOverloads
@Composable
fun  HomeActivityNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    getState: (action: HomeViewModelGetStateAction) -> StateFlow<*>,
    setState: (action: HomeViewModelSetStateAction) -> Unit,
    musicLazyPaging: ()-> Flow<PagingData<MusicFile>>
) {
    val navigate = navController.navigateActionSetUp(setState=setState)
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
                navigate = navigate,
                setState = setState,
                getState = getState
            )
        }
        composable(
            route = Destination.AllMusicScreen.route
        ) {
            val lazyListState by getState(HomeViewModelGetStateAction.GetLazyListState).collectAsState()
            PlaylistPage(
                lazyListState = lazyListState as LazyListState,
                pageDataItem = musicLazyPaging().collectAsLazyPagingItems(),
                setState = setState
            )
        }
        composable(
            route = Destination.MostPlayedScreen.route
        ) {
            val lazyListState by getState(HomeViewModelGetStateAction.GetLazyListState).collectAsState()
            PlaylistPage(
                lazyListState = lazyListState as LazyListState,
                pageDataItem = musicLazyPaging().collectAsLazyPagingItems(),
                setState = setState
            )
        }
        composable(
            route = Destination.RecentlyPlayedScreen.route
        ) {
            val lazyListState by getState(HomeViewModelGetStateAction.GetLazyListState).collectAsState()
            PlaylistPage(
                lazyListState = lazyListState as LazyListState,
                pageDataItem = musicLazyPaging().collectAsLazyPagingItems(),
                setState = setState
            )
        }
        composable(
            route = Destination.PlaylistScreen.route,
        ) {
            val lazyListState by getState(HomeViewModelGetStateAction.GetLazyListState).collectAsState()
            PlaylistPage(
                lazyListState = lazyListState as LazyListState,
                pageDataItem = musicLazyPaging().collectAsLazyPagingItems(),
                setState = setState
            )
        }
        composable(
            route = Destination.FavoriteMusicScreen.route,
        ) {
            val lazyListState by getState(HomeViewModelGetStateAction.GetLazyListState).collectAsState()
            PlaylistPage(
                lazyListState = lazyListState as LazyListState,
                pageDataItem = musicLazyPaging().collectAsLazyPagingItems(),
                setState = setState
            )
        }
        composable(
            route = Destination.FolderScreen.route
        ){
            FolderPage(
                navigate = navigate,
                getState = getState
            )
        }
    }
}




