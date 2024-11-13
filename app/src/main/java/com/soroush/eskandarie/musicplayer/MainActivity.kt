package com.soroush.eskandarie.musicplayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.soroush.eskandarie.musicplayer.presentation.ui.model.PlaylistDropdownItem
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components.PlaylistItem
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            MusicPlayerTheme {


                PlaylistItem(
                    title = "Playlist 1",
                    posterUri = Uri.parse("sht"),
                    onIcon1Click = {},
                    onIcon2Click = {},
                    dropdownList = listOf(PlaylistDropdownItem(1, "rename", {}), PlaylistDropdownItem(2, "delete", {})),
                    playlistId = 1,
                    onClick = {}
                )
            }
        }
    }
}
