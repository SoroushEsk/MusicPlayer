package com.soroush.eskandarie.musicplayer.util

object Constants {
    object SplashScreen{
        const val SplashDuration                : Long   = 2500
        const val SplashScreenIconDescription   : String = "music_player_icon"
        const val SplashScreenAppName           : String = "Music Player"
    }
    object HomePageValues{
        const val PlaylistSectionTitle          : String = "Playlists"
        const val MusicPageMotionLayoutId       : String = "music_bar_motion_layout"
    }
    object MusicBarValues{
        const val MotionLayoutContainerId       : String = "motion_container"
        const val MotionLayoutPosterId          : String = "motion_music_poster"
        const val MotionLayoutBackIconId        : String = "motion_back_icon"
        const val MotionLayoutPlayIconId        : String = "motion_play_icon"
        const val MotionLayoutTitleId           : String = "motion_music_title"
        const val MotionLayoutArtistId          : String = "motion_music_artist"
        const val MotionLayoutForwardIconId     : String = "motion_forward_icon"
        const val MotionLayoutPlaylistIconId    : String = "motion_playlist_icon"
        const val MotionLayoutTextContainer     : String = "motion_text_container"
        const val PosterHideDuration            : Int    = 300

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

        const val MusicPosterToDiskRatio        : Float = 142f / 255f
        const val DiskNeedleRotationCenterYRatio: Float = 18f  / 211f
        const val DiskNeedleRotationCenterXRatio: Float = 18f  / 150f
        const val DiskRotationDuration          : Int   = 20000
        const val NeedleRotationDuration        : Int   = 1000
        const val DiskAndNeedleRevealDuration   : Int   = 900
        const val LeaveScrollDuration           : Int   = 900
        const val ProgressBarPointToStrokeRatio : Float = 1.8f
    }
    object PlaybackAction{
        const val Pause                         : String = "com.soroush.eskandarie.musicplayer.PAUSE"
        const val Resume                        : String = "com.soroush.eskandarie.musicplayer.RESUME"

    }

}