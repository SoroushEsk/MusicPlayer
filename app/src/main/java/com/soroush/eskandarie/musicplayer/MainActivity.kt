package com.soroush.eskandarie.musicplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.soroush.eskandarie.musicplayer.presentation.ui.page.common.SearchField
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.HomeActivity
import com.soroush.eskandarie.musicplayer.presentation.ui.page.music.MusicPage
import com.soroush.eskandarie.musicplayer.presentation.ui.page.music.ProfileHeader
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        setContent {
            val context = LocalContext.current
            MusicPlayerTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.Blue
//                ) {
//                    Column {
//                        var progress by remember { mutableStateOf(0f) }
//
//                        ProfileHeader(progress = progress)
//                        Spacer(modifier = Modifier.height(32.dp))
//                        Slider(
//                            value = progress,
//                            onValueChange = { progress = it },
//                            modifier = Modifier.padding(horizontal = 32.dp)
//                        )
//                    }
//                }
            }
        }
    }
}
