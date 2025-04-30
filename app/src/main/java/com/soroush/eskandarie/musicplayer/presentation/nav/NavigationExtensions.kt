package com.soroush.eskandarie.musicplayer.presentation.nav

import androidx.navigation.NavController
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction.*
import com.soroush.eskandarie.musicplayer.presentation.state.PlaylistType

fun NavController.navigateActionSetUp(
    setState: (actin: HomeViewModelSetStateAction) -> Unit
): (navAction: NavControllerAction) -> Unit{
    return { navAction ->
        when (navAction) {
            is NavigateToPlaylist -> {
                setState(
                    HomeViewModelSetStateAction.SetUpMusicList(
                        id = navAction.playlistId,
                        route = navAction.route
                    )
                )
                setState(HomeViewModelSetStateAction.SetCurrentPlaylist(
                    PlaylistType.UserPlayList(
                        id = navAction.playlistId,
                        name = navAction.name
                    )
                ))
                navigate(navAction.route)
            }
            is NavigateToAllMusic -> {
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                setState(HomeViewModelSetStateAction.SetCurrentPlaylist(
                    PlaylistType.TopPlaylist(
                        route = navAction.route,
                        name = "All Tracks"
                    )
                ))
                navigate(navAction.route)
            }
            is NavigateToMostPlayed ->{
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                setState(HomeViewModelSetStateAction.SetCurrentPlaylist(
                    PlaylistType.TopPlaylist(
                        route = navAction.route,
                        name = "Most Played"
                    )
                ))
                navigate(navAction.route)
            }
            is NavigateToRecentlyPlayed -> {
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                setState(HomeViewModelSetStateAction.SetCurrentPlaylist(
                    PlaylistType.TopPlaylist(
                        route = navAction.route,
                        name = "Recently Played"
                    )
                ))
                navigate(navAction.route)
            }
            is NavigateToFolders ->{
                navigate(navAction.route)
            }
            is NavigateToFavorite ->{
                setState(HomeViewModelSetStateAction.SetUpMusicList( route = navAction.route))
                setState(HomeViewModelSetStateAction.SetCurrentPlaylist(
                    PlaylistType.TopPlaylist(
                        route = navAction.route,
                        name = "Favorite"
                    )
                ))
                navigate(navAction.route)
            }
            is NavigateToFolderMusic ->{
                setState(HomeViewModelSetStateAction.SetUpMusicList(
                    folderName = navAction.folderName,
                    route = navAction.route
                ))
                setState(HomeViewModelSetStateAction.SetCurrentPlaylist(
                    PlaylistType.FolderPlaylist(
                        folderName = navAction.folderName,
                        name = navAction.folderName
                    )
                ))
                navigate(navAction.route)
            }
        }
    }
}

