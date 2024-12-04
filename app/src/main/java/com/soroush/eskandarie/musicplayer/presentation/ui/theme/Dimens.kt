package com.soroush.eskandarie.musicplayer.presentation.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dimens {
    object CornerRadius{
        val General                                     : Dp    = 12.dp
        val TopPlaylistItemCornerRadius                 : Dp    = 16.dp
        val AppTextFieldCornerRadius                    : Dp    = 12.dp
        val MusicBar                                    : Dp    = 48.dp
    }
    object Padding{
        val HomeActivity                                : Dp    = 10.dp
        val TopPlaylistItemDefault                      : Dp    =  6.dp
        val PlayListItemIconSize                        : Dp    = 16.dp
        val PlayListItemDropdown                        : Dp    =  4.dp
        val PlaylistItemPoster                          : Dp    =  8.dp
        val PlaylistItemIcon                            : Dp    =  2.dp
        val HomePagePadding                             : Dp    =  8.dp
        val HomePageTopPlaylistItem                     : Dp    = 10.dp
        val MusicBarMusicPoster                         : Dp    =  6.dp

        val MusicBarMotionLayoutTextContainer           : Dp    =  4.dp
        val MusicBarMotionLayoutControlIcon             : Dp    =  5.dp
        val MusicBarMotionLayoutPlaylistIcon            : Dp    =  4.dp
        val MusicBarMotionLayoutGeneral                 : Dp    = 20.dp
    }
    object Spacing{
        val HomePageSpaceBetween                        : Dp    =  4.dp
        val PlaylistItemDropdownSpace                   : Dp    =  2.dp

        val MusicPageSpaceBetween                       : Dp    = 12.dp
    }
    object Rotation {
        val TopPlaylistItemBackRotation                 : Float = 4f
    }
    object Size{
        val TopPlaylistItemTextWeight                   : Float = 0.15f
        val TopPlaylistItemBackWeight                   : Float = 0.85f
        val GeneralItemHeight                           : Dp    = 64.dp
        val PlaylistSpaceBetween                        : Dp    = 10.dp     // TODO("fix it should be in the spacing"
        val PlayListItemIconSize                        : Dp    = 18.dp
        val MinMusicBarHeight                           : Dp    = 80.dp

        val MusicPageAboveProgressBarHeight             : Dp    = 80.dp


        val MusicBarMotionLayoutContainerHeight         : Dp    = 64.dp
        val MusicBarMotionLayoutPosterHeight            : Dp    = 60.dp
        val MusicBarMotionLayoutTextContainerHeight     : Dp    = 64.dp
        val MusicBarMotionLayoutBackForward             : Dp    = 26.dp
        val MusicBarMotionLayoutPauseResume             : Dp    = 36.dp
        val MusicBarMotionLayoutPlaylistIcon            : Dp    = 32.dp

    }
}