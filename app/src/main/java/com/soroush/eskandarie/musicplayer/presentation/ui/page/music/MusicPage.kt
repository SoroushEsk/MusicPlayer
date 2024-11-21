package com.soroush.eskandarie.musicplayer.presentation.ui.page.music

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.util.Constaints
import okio.ByteString.Companion.readByteString

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MusicPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val context = LocalContext.current

    val motionProgress = scrollState.value.toFloat() / scrollState.maxValue.toFloat().coerceAtLeast(1f)
    val motionSceneContent = remember { 
        context.resources.openRawResource(R.raw.music_page_constraint_set).bufferedReader().use { it.readText() 
        }
    }
    val motionScene = MotionScene(content = motionSceneContent)
    MotionLayout(
        modifier = Modifier
            .layoutId(Constaints.HomePageValues.MusicPageMotionLayoutId),
        motionScene = motionScene,
        progress = motionProgress
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId(Constaints.MusicBarValues.MotionLayoutBoxId)
    )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProfileHeader(
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    val progress = remember { derivedStateOf {
        (scrollState.value / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)
    }}
    val context = LocalContext.current
    val scene = remember {
        try {
            context.resources.openRawResource(R.raw.music_page_constraint_set).readBytes().decodeToString()
        } catch (e: Exception) {
            Log.e("ProfileHeader", "Error loading motion scene", e)
            "" // Provide a fallback empty string
        }
    }

    MotionLayout(
        motionScene = MotionScene(content = scene),
        progress = progress.value,
        modifier = modifier.fillMaxWidth().fillMaxHeight()
    ) {
        val profilePictureProperties = motionProperties(id = "profile_pic")
        val usernameProperties = motionProperties(id = "username")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId("box")
                .verticalScroll(scrollState)
        ){

            Image(
                painter = painterResource(id = R.drawable.ed),
                contentDescription = "profile_pic",
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = profilePictureProperties.value.color("background"),
                        shape = CircleShape
                    )
                    .layoutId("profile_pic")
            )
            Text(
                text = "Slim Shady",
                fontSize = 24.sp,
                color = usernameProperties.value.color("background"),
                modifier = Modifier.layoutId("username")
            )
        }

    }
}