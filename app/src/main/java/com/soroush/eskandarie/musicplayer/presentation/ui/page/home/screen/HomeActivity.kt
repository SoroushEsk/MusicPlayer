package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.Playlist
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.music.MusicPage
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val viewmodel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

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
                    )

//                MusicPage(scrollState = scrollState)
            }
        }
    }


    //region Init Methods

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