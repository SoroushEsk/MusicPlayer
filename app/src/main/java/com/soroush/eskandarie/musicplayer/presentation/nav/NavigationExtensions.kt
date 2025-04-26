package com.soroush.eskandarie.musicplayer.presentation.nav

import androidx.navigation.NavController
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction.*

fun NavController.navigateActionSetUp(
    setState: (actin: HomeViewModelSetStateAction) -> Unit
): (navAction: NavControllerAction) -> Unit{
    return { navAction ->
        when (navAction) {
            is NavigateToPlaylist -> {
                setState(
                    HomeViewModelSetStateAction.SetUpMusicList(
                        navAction.playlistId,
                        navAction.route
                    )
                )
                navigate(navAction.route)
            }
            is NavigateToAllMusic -> {
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                navigate(navAction.route)
            }
            is NavigateToMostPlayed ->{
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                navigate(navAction.route)
            }
            is NavigateToRecentlyPlayed -> {
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                navigate(navAction.route)
            }
            is NavigateToFolders ->{

                navigate(navAction.route)
            }
            is NavigateToFavorite ->{
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                navigate(navAction.route)
            }
            else -> {}
        }
    }
}

