package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.soroush.eskandarie.musicplayer.presentation.action.HomeGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.ui.page.common.SearchField
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePlaylist
import com.soroush.eskandarie.musicplayer.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                    getState =
                        viewmodel.getHomeState(HomeGetStateAction.GetSearchTextState)

                ) {

                }

                HomePlaylist(
                    modifier = Modifier
                )
            }
        }
    }


    //region Init Methods

    //endregion
}