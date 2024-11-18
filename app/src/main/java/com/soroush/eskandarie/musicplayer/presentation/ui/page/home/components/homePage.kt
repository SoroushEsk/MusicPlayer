package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomePlaylist(
    modifier: Modifier = Modifier,


) {
    var scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .aspectRatio(1f)
            .border(BorderStroke(2.dp, Color.Black))
        ){
            Column(

            ){
                Row (
                    modifier = Modifier
                        .weight(1f)
                ){

                }
                Row (
                    modifier = Modifier
                        .weight(1f)
                ){

                }
            }
        }
    }
}