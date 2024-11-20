package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.Playlist
import com.soroush.eskandarie.musicplayer.presentation.action.HomeGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.ui.page.common.SearchField
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePlaylist
import com.soroush.eskandarie.musicplayer.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val viewmodel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                SearchField(
                    setState = viewmodel::getHomeSetAction,
                    getState = viewmodel.homeState.map {homeState ->
                            homeState.searchFieldState.searchText
                        }.collectAsState(initial = "")
                ) {

                }

                HomePlaylist(
                    modifier = Modifier,
                    playlists = getPlaylist()
                )
            }
        }
    }


    //region Init Methods

    //endregion
    //region Normal Methods
    private fun getPlaylist() : List<Playlist> = listOf(
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