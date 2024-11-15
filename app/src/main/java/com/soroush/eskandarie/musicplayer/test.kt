package com.soroush.eskandarie.musicplayer

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent(scaffoldState)
        },
        sheetPeekHeight = 64.dp
    ) {
        MainContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(scaffoldState: BottomSheetScaffoldState) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color.LightGray)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (dragAmount.y < 0) {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        } else if (dragAmount.y > 0) {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }
                    }
                }
        ) {
            Text(
                text = "Slide up for more controls",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Music Controls", fontSize = 24.sp)

        // Add music control buttons and other elements here
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { /* Play */ }) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
            }
            IconButton(onClick = { /* Pause */ }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Pause")
            }
            IconButton(onClick = { /* Next */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Next")
            }
        }
    }
}

@Composable
fun MainContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Music Player", color = Color.White, fontSize = 32.sp)
    }
}
