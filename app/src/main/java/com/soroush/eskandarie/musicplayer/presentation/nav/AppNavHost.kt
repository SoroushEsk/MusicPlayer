package com.soroush.eskandarie.musicplayer.presentation.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.HomePage
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens

@Composable
fun HomeActivityNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val startDestination: String = Destination.HomeScreen.route

    NavHost(
        navController = navController,
        startDestination = Destination.HomeScreen.route,
        modifier = modifier
    ) {
        composable (route = Destination.HomeScreen.route ){
            HomePage(
                modifier = Modifier,
                playlists = listOf()
            )
        }
    }


}