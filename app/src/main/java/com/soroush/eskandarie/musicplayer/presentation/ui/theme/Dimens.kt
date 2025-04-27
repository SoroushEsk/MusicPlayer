package com.soroush.eskandarie.musicplayer.presentation.ui.theme

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dimens {
    object Size{
        val FolderItemBorderWidth                           : Dp        =   2.dp
        val FolderPageFolderItemHieght                      : Dp        =  64.dp
        val HomePagePlaylistTitleContainerHeight            : Dp        =  40.dp
        val MusicItemShadowBorder                           : Dp        =   2.dp
        val MusicItemHeight                                 : Dp        =  80.dp
        val MusicItemPlayStatusIcon                         : Float     =  0.5f
        val SplashScreenIcon                                : Float     =  0.45f
        val TopPlaylistItemTextWeight                       : Float     =  0.15f
        val TopPlaylistItemBackWeight                       : Float     =  0.85f
        val GeneralItemHeight                               : Dp        =  64.dp
        val PlaylistSpaceBetween                            : Dp        =  10.dp     // TODO("fix it should be in the spacing"
        val PlayListItemIconSize                            : Dp        =  18.dp
        val MinMusicBarHeight                               : Dp        =  80.dp
        val MusicPageAboveProgressBarHeight                 : Dp        =  80.dp
        val MusicBarMotionLayoutContainerHeight             : Int       =  68
        val MusicBarMotionLayoutPosterHeight                : Int       =  60
        val MusicBarMotionLayoutTextContainerHeight         : Int       =  64
        val MusicBarMotionLayoutBackForward                 : Int       =  26
        val MusicBarMotionLayoutPauseResume                 : Int       =  36
        val MusicBarMotionLayoutPlaylistIcon                : Int       =  32
        val MusicPageMotionLayoutTextContainerHeightRatio   : String    = "10%"
        val MusicPageMotionLayoutTextContainerWidthRatio    : String    = "60%"
        val MusicPageMotionLayoutPlayResumeHeightRatio      : String    = "8%"
        val MusicPageMotionLayoutForwardBackHeightRatio     : String    = "6%"
        val MusicPageMotionLayoutIconsHeight                : Int       =  76
        val MusicPageMotionPosterMaxSize                    : Int       = 100
        val MusicPageShadowedIconLightBorder                : Dp        =   2.dp
        val MusicPageShadowedIconDarkBorder                 : Dp        =   1.dp
        val MusicPageDiskSizeRatio                          : Float     = 0.8f
        val MusicPageNeedleHeightRatio                      : Float     = 0.4f
        val MusicPageProgressBarStrokeWidth                 : Dp        =  10.dp
    }
    object Padding{
        val Zero                                            : Dp        =   0.dp
        val FolderPageFolderItemIconPadding                 : Dp        =   8.dp
        val MusicItemArtistTextEndPadding                   : Dp        =  20.dp
        val MusicItemTextContainerHorizontal                : Dp        =  10.dp
        val MusicItemTextContainerVertical                  : Dp        =  16.dp
        val MusicItemPosterInsidePadding                    : Dp        =  10.dp
        val SplashScreenTextDown                            : Dp        =  60.dp
        val HomeActivity                                    : Dp        =  10.dp
        val TopPlaylistItemDefault                          : Dp        =   6.dp
        val PlayListItemIconSize                            : Dp        =  16.dp
        val FolderPagePadding                               : Dp        = HomeActivity
        val PlayListItemDropdown                            : Dp        =   4.dp
        val PlaylistItemPoster                              : Dp        =   8.dp
        val PlaylistItemIcon                                : Dp        =   2.dp
        val HomePagePadding                                 : Dp        =   8.dp
        val HomePageTopPlaylistItem                         : Dp        =  10.dp
        val HomePageAddPlaylistIcon                         : Dp        =   8.dp
        val MusicBarMusicPoster                             : Dp        =   6.dp
        val MusicBarMotionLayoutTextContainer               : Int       =   4
        val MusicBarMotionLayoutControlIcon                 : Int       =   5
        val MusicBarMotionLayoutPlaylistIcon                : Int       =   4
        val MusicBarMotionLayoutGeneral                     : Int       =  28
        val MusicPageMotionLayoutPlayResumeTop              : Int       = 100
        val MusicPageShadowedIconDefault                    : Dp        =  10.dp
        val MusicPageIconsAtEndRowDefault                   : Dp        =  16.dp
        val MusicPageProgressBarDefaultHorizontal           : Dp        =  16.dp
        val MusicPageProgressBarDefaultVertical             : Dp        =   0.dp
        val MusicPageProgressBarCanvasHorizontal            : Dp        =  16.dp
        val MusicPagePlayControlShadowDefault               : Dp        =  10.dp

    }
    object CornerRadius{
        val TextFieldAddPlaylist                            : Dp        =  12.dp
        val General                                         : Dp        =  26.dp
        val FolderItem                                      : Dp        =  26.dp
        val TopPlaylistItem                                 : Dp        =  26.dp
        val AppTextField                                    : Dp        =  26.dp
        val MusicBar                                        : Dp        =  48.dp
        val MusicPageShadowedIconDefault                    : Dp        =  26.dp
        val MusicPagePlayControlShadowIconDefault           : Dp        =  26.dp
        val MusicItemDefault                                : Dp        =  26.dp
    }
    object Spacing{

        val MusicBarMotionLayoutContainerHeight             : Dp        =  68.dp
        val FolderItemIconFolderName                        : Dp        =   8.dp
        val HomePageSpaceBetween                            : Dp        =   8.dp
        val PlaylistItemDropdownSpace                       : Dp        =   2.dp
        val MusicPageSpaceBetween                           : Dp        =  12.dp
    }
    object Rotation {
        val TopPlaylistItemBackRotation                     : Float     =   4f
        val MusicPageNeedleBase                             : Float     =  60f
        val MusicPageNeedleInitial                          : Float     = -45f
        val MusicPageNeedleFinal                            : Float     = -15f
    }
    object Alpha {
        val FolderItemBorderDark                            : Float     = 0.3f
        val MusicPageDiskPoster                             : Float     = 0.8f
        val MusicPageShadowedIconBackground                 : Float     =0.15f
        val DomainColor1Alpha                               : Float     = 0.6f
        val DomainColor2Alpha                               : Float     = 0.5f
        val VibrantColor1Alpha                              : Float     = 0.4f
        val VibrantColor2Alpha                              : Float     = 0.2f
        val MutedColorAlpha                                 : Float     = 0.1f
        val MusicItemPlayStatusIcon                         : Float     = 0.8f
        val MusicItemFadeText                               : Float     = 0.6f
        val MusicItemLightShadowBorderColor                 : Float     = 0.3f
        val MusicItemDarkShadowBorderColor                  : Float     = 0.4f
    }
    object Elevation {
        val MusicPageDiskPoster                             : Dp        =  10.dp
        val MusicPageNeedle                                 : Dp        =   8.dp
        val MusicPagePlayControlShadowDarkLarge             : Dp        =   4.dp
        val MusicPagePlayControlShadowDarkMedium            : Dp        =   2.dp

    }
    object Scale {
        val MusicPageSongPosterNeedle                       : Float     = 0.4f
    }
    object AspectRatio {
        val MusicPageNeedle                                 : Float     = 0.6f
        val SplashIcon                                      : Float     = 1f
        val MusicItemPoster                                 : Float     = 1f
        val AddNewPlaylistButton                            : Float     = 1f
    }
    object Offset{
        val MusicPageNeedleXOffset                          : Dp        = -16.dp
        val MusicPageNeedleYOffset                          : Dp        =  32.dp
    }
}