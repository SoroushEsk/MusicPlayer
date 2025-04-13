package com.soroush.eskandarie.musicplayer.presentation.nav

import android.content.Context
import android.net.Uri
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
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.model.Playlist
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDatabaseUseCase
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.PlaylistPage
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

@Composable
fun HomeActivityNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    getLazyListState: @Composable () -> State<LazyListState>,
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
                playlists = getPlaylist(localContext),
                navController = navController
            )
        }
        composable(route = Destination.AllMusicScreen.route) {
            LaunchedEffect (Unit){
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

private fun getPlaylist(context: Context): List<Playlist> = listOf(
    Playlist(
        0,
        "Favorite",
        Uri.parse("android.resource://${context.packageName}/" + R.drawable.favorite_playlist)
    ),
    Playlist(
        1,
        "Playlist 1",
        Uri.parse("android.resource://${context.packageName}/" + R.drawable.shaj)
    ),
    Playlist(
        2,
        "Playlist 2",
        Uri.parse("android.resource://${context.packageName}/" + R.drawable.ghader)
    ),
    Playlist(
        3,
        "Playlist 3",
        Uri.parse("android.resource://${context.packageName}/" + R.drawable.simint)
    ),
    Playlist(
        4,
        "Playlist 4",
        Uri.parse("android.resource://${context.packageName}/" + R.drawable.sharhram)
    ),
    Playlist(
        5,
        "Playlist 5",
        Uri.parse("android.resource://${context.packageName}/" + R.drawable.empty_album)
    )
)


