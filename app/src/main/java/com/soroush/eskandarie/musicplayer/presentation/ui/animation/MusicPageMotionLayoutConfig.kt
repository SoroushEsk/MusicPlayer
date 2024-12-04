package com.soroush.eskandarie.musicplayer.presentation.ui.animation

import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens

val musicPageMotionLayoutConfig = """
{
  "ConstraintSets": {
    "start": {
      "motion_container": {
        "width": "parent",
        "height": ${Dimens.Size.MusicBarMotionLayoutContainerHeight},
        "bottom": ["parent", "bottom", 0],
        "start": ["parent", "start", 0],
        "end": ["parent", "end", 0]
      },
      "down_option_icon_container": {
        "height": 0,
        "top": ["motion_text_container", "top", 0],
        "bottom": ["motion_text_container", "bottom", 0]
      },
      "motion_text_container": {
        "width": "spread",
        "height": ${Dimens.Size.MusicBarMotionLayoutTextContainerHeight},
        "end": ["motion_back_icon", "start", ${Dimens.Padding.MusicBarMotionLayoutTextContainer}],
        "start": ["motion_music_poster", "end", ${Dimens.Padding.MusicBarMotionLayoutTextContainer}],
        "top": ["motion_container", "top"],
        "bottom": ["motion_container", "bottom", 0]
      },
      "motion_music_poster": {
        "height": ${Dimens.Size.MusicBarMotionLayoutPosterHeight},
        "start": ["motion_container", "start", 0],
        "top": ["motion_container", "top"],
        "bottom": ["motion_container", "bottom", 0]
      },
      "above_progress_bar_container": {
        "height": 0,
        "top": ["motion_music_poster", "bottom", ${Dimens.Padding.MusicBarMotionLayoutGeneral}],
        "bottom": ["motion_play_icon", "top", ${Dimens.Padding.MusicBarMotionLayoutGeneral}]
      },
      "play_progress_bar_container": {
        "height": 0,
        "width": 0,
        "start": ["motion_music_poster", "start", 0],
        "end": ["motion_music_poster", "end", 0],
        "top": ["motion_music_poster", "bottom", ${Dimens.Padding.MusicBarMotionLayoutGeneral}],
        "bottom": ["motion_play_icon", "top", ${Dimens.Padding.MusicBarMotionLayoutGeneral}]
      },
      "music_rotate_disk": {
        "height": ${Dimens.Size.MusicBarMotionLayoutPosterHeight},
        "start": ["motion_container", "start", 0],
        "top": ["motion_container", "top"],
        "bottom": ["motion_container", "bottom", 0]
      },
      "shuffle_repeat_container": {
        "height": 0,
        "top": ["motion_play_icon", "bottom", 0],
        "bottom": ["motion_play_icon", "top", 0]
      ,
      "motion_back_icon": {
        "height": ${Dimens.Size.MusicBarMotionLayoutBackForward},
        "end": ["motion_play_icon", "start", ${Dimens.Padding.MusicBarMotionLayoutControlIcon}],
        "top": ["motion_container", "top"],
        "bottom": ["motion_container", "bottom", 0]
      },
      "motion_play_icon": {
        "height": ${Dimens.Size.MusicBarMotionLayoutPauseResume},
        "end": ["motion_forward_icon", "start", ${Dimens.Padding.MusicBarMotionLayoutControlIcon}],
        "top": ["motion_container", "top"],
        "bottom": ["motion_container", "bottom", 0]
      },
      "motion_forward_icon": {
        "height": ${Dimens.Size.MusicBarMotionLayoutBackForward},
        "end": ["motion_playlist_icon", "start", ${Dimens.Padding.MusicBarMotionLayoutControlIcon}],
        "top": ["motion_container", "top"],
        "bottom": ["motion_container", "bottom", 0]
      },
      "motion_playlist_icon": {
        "height": ${Dimens.Size.MusicBarMotionLayoutPlaylistIcon},
        "end": ["parent", "end", ${Dimens.Padding.MusicBarMotionLayoutPlaylistIcon}],
        "top": ["motion_container", "top"],
        "bottom": ["motion_container", "bottom", 0]
      }}
    },
    "end": {
      "motion_container": {
        "width": "parent",
        "height": "parent",
        "bottom": ["parent", "bottom", 0],
        "start": ["parent", "start", 0],
        "end": ["parent", "end", 0],
        "top": ["parent", "top", 0]
      },
      "motion_text_container": {
        "width": "60%",
        "height": "10%",
        "end": ["parent", "end", 0],
        "start": ["parent", "start", 0],
        "top": ["motion_container", "top", 10]
      },
      "down_option_icon_container": {
        "height": 72,
        "top": ["motion_text_container", "top", 0],
        "bottom": ["motion_text_container", "bottom", 0]
      },
      "motion_music_poster": {
        "width": 100,
        "top": ["motion_text_container", "bottom", 0],
        "start": ["motion_container", "start", 0],
        "end": ["motion_container", "end", 0]
      },
      "music_rotate_disk": {
        "width": "parent",
        "top": ["motion_text_container", "bottom", 0]
      },
      "above_progress_bar_container": {
        "height": 72,
        "top": ["music_rotate_disk", "bottom", 20],
        "bottom": ["play_progress_bar_container", "top", 0]
      },
      "play_progress_bar_container": {
        "width": "parent",
        "bottom": ["motion_play_icon", "top", 20],
        "top": ["above_progress_bar_container", "bottom", 0]
      },
      "motion_back_icon": {
        "height": "6%",
        "end": ["motion_play_icon", "start", 20],
        "bottom": ["motion_play_icon", "bottom", 0],
        "top": ["motion_play_icon", "top", 0]
      },
      "motion_play_icon": {
        "height": "8%",
        "start": ["parent", "start", 0],
        "end": ["parent", "end", 0],
        "top": ["play_progress_bar_container", "bottom", 0],
        "bottom": ["motion_container", "bottom", 100]
      },
      "motion_forward_icon": {
        "height": "6%",
        "start": ["motion_play_icon", "end", 20],
        "bottom": ["motion_play_icon", "bottom", 0],
        "top": ["motion_play_icon", "top", 0]
      },
      "shuffle_repeat_container": {
        "height": 72,
        "top": ["motion_play_icon", "bottom", 0],
        "bottom": ["motion_play_icon", "top", 0]
      },
      "motion_playlist_icon": {
        "height": 32,
        "end": ["parent", "end", 4],
        "bottom": ["motion_container", "bottom", 0],
        "alpha": 0
      }
    }
  },
  "Transitions": {
    "default": {
      "from": "start",
      "to": "end",
      "pathMotionArc": "startVertical",
      "KeyFrames": {
        "KeyPositions": [
          {
            "target": ["motion_music_poster"],
            "progress": [0.5, 1]
          }
        ],
        "KeyAttributes": [
          {
            "target": ["motion_music_poster"],
            "frames": [0, 25, 50, 75, 100],
            "progress": [0, 0.2, 0.4, 0.6, 0.8, 1]
          }
        ]
      }
    }
  }
}
""".trimIndent()
