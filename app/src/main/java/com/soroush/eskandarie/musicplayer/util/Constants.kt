package com.soroush.eskandarie.musicplayer.util

import android.widget.ProgressBar

object Constants {
    object HomePageValues{
        const val PlaylistSectionTitle      : String = "Playlists"
        const val MusicPageMotionLayoutId   : String = "music_bar_motion_layout"
    }
    object MusicBarValues{
        const val MotionLayoutContainerId   : String = "motion_container"
        const val MotionLayoutPosterId      : String = "motion_music_poster"
        const val MotionLayoutBackIconId    : String = "motion_back_icon"
        const val MotionLayoutPlayIconId    : String = "motion_play_icon"
        const val MotionLayoutTitleId       : String = "motion_music_title"
        const val MotionLayoutArtistId      : String = "motion_music_artist"
        const val MotionLayoutForwardIconId : String = "motion_forward_icon"
        const val MotionLayoutPlaylistIconId: String = "motion_playlist_icon"
        const val MotionLayoutTextContainer : String = "motion_text_container"

    }
    object MusicPageValues{
        const val MusicPosterDescription        : String = "Music Poster"
        const val PlayProgressBarContainerId    : String = "play_progress_bar_container"
        const val ProgressBarTotalDurationId    : String = "progress_bar_total_duration"
        const val ProgressBar                   : String = "progress_bar"
        const val AboveProgressBarContainerId   : String = "above_progress_bar_container"
        const val MusicRotateDiskId             : String = "music_rotate_disk"
        const val ShuffleRepeatContainerId      : String = "shuffle_repeat_container"
        const val DownOptionIconContainerId     : String = "down_option_icon_container"

        const val MusicPosterToDiskRatio        : Float = 142 / 255f
    }


}