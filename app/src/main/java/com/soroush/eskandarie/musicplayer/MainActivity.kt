package com.soroush.eskandarie.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.soroush.eskandarie.musicplayer.presentation.ui.page.common.SearchField
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            MusicPlayerTheme {

                SearchField {

                }
            }
        }
    }
}
