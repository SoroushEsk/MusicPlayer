@file:Suppress("DEPRECATION")

package com.soroush.eskandarie.musicplayer.shared_component.media_controller

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.coroutineScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.MediaController.Listener
import androidx.media3.session.SessionToken
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaControllerObserver(
    private val context: Context,
    private val sessionToken: SessionToken,
    private val lifecycle: Lifecycle,
    private val changeViewModelState: (action: HomeViewModelSetStateAction)->Unit
) : LifecycleObserver {
    private lateinit var mediaController: MediaController

    init {
        lifecycle.addObserver(this)
        initialMediaController()
    }

    private fun initialMediaController() {
        lifecycle.coroutineScope.launch {
            try {
                val controller = withContext(Dispatchers.Main) {
                    MediaController.Builder(context, sessionToken)
                        .buildAsync()
                }
                mediaController = controller.get()
                registerListener()
            }catch (e: Exception){
                e.stackTraceToString()
            }
        }
    }

    private fun registerListener(){
        mediaController.addListener(
            object: Listener, Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)

                }
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    changeViewModelState(HomeViewModelSetStateAction.SetPlayState(isPlaying))
                }
            }
        )

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun releaseController() {
        mediaController.release()
        lifecycle.removeObserver(this)
    }
}