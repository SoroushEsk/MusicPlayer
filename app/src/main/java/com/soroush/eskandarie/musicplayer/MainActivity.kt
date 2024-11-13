package com.soroush.eskandarie.musicplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.presentation.ui.model.PlaylistDropdownItem
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.PlaylistItem
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.PlaylistPoster
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Light
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            MusicPlayerTheme {
                Surface( modifier = Modifier
                    .fillMaxSize()
                    .background(Light.Background)) {
                    Column (modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                    ){
                        PlaylistItem(
                            title = "Playlist 1",
                            posterUri = Uri.parse("sht"),
                            onIcon1Click = {},
                            onIcon2Click = {},
                            dropdownList = listOf(PlaylistDropdownItem(1, "rename", {}), PlaylistDropdownItem(2, "delete", {})),
                            playlistId = 1,
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
                        PlaylistItem(
                            title = "Playlist 2",
                            posterUri = Uri.parse("sht"),
                            onIcon1Click = {},
                            onIcon2Click = {},
                            dropdownList = listOf(PlaylistDropdownItem(1, "rename", {}), PlaylistDropdownItem(2, "delete", {})),
                            playlistId = 1,
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
                        PlaylistItem(
                            title = "Playlist 3",
                            posterUri = Uri.parse("sht"),
                            onIcon1Click = {},
                            onIcon2Click = {},
                            dropdownList = listOf(PlaylistDropdownItem(1, "rename", {}), PlaylistDropdownItem(2, "delete", {})),
                            playlistId = 1,
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
                        PlaylistItem(
                            title = "Playlist 4",
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
    }
}
