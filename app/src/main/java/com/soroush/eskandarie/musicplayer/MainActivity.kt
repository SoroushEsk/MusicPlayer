package com.soroush.eskandarie.musicplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.coroutineScope
import com.soroush.eskandarie.musicplayer.presentation.ui.page.common.SearchField
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen.HomeActivity
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val intent = Intent(this, HomeActivity::class.java)
        CoroutineScope(this.lifecycle.coroutineScope.coroutineContext).launch { delay(20000)
            startActivity(intent)
        }
        setContent {
            val context = LocalContext.current
            MusicPlayerTheme {

                MusicPlayerScreen()
            }
        }
    }
}
