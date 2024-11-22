package com.soroush.eskandarie.musicplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.HomeActivity
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme

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
