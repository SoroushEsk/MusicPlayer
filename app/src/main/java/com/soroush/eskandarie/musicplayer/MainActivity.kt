package com.soroush.eskandarie.musicplayer

import android.net.Uri
import android.os.Bundle
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.presentation.ui.page.home.PlaylistPoster
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            MusicPlayerTheme {
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .aspectRatio(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ){
                    repeat(2){
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            repeat(2) {
                                PlaylistPoster(
                                    extraPadding = 4.dp,
                                    title = "Favorite",
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(Color.Gray.copy(alpha = 0.25f)),
                                    imageShape = RoundedCornerShape(10.dp),
                                    uriFront = Uri.parse("android.resource://${LocalContext.current.packageName}/${R.drawable.ed}"),
                                    uriBack1 = Uri.parse("android.resource://${LocalContext.current.packageName}/${R.drawable.mahasti}"),
                                    uriBack2 = Uri.parse("android.resource://${LocalContext.current.packageName}/${R.drawable.sandi}")
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
