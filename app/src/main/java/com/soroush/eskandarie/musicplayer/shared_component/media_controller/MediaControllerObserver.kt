@file:Suppress("DEPRECATION")

package com.soroush.eskandarie.musicplayer.shared_component.media_controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import com.soroush.eskandarie.musicplayer.R
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
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
    lateinit var mediaController: MediaController
        private set

    init {
        lifecycle.addObserver(this)
        initialMediaController()
    }

    private fun initialMediaController() {
        lifecycle.coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val controller = MediaController.Builder(context, sessionToken)
                        .buildAsync()
                    mediaController = controller.get()
                    changeViewModelState(
                        HomeViewModelSetStateAction.SetMediaControllerObserver(mediaController)
                    )
                    withContext(Dispatchers.Main) {
                        registerListener()
                    }
                }
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
                    changeViewModelState(HomeViewModelSetStateAction.UpdateMusicDetails)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    if ( reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO){
                       changeViewModelState(HomeViewModelSetStateAction.OnNextMusic)
                    }else changeViewModelState(HomeViewModelSetStateAction.UpdateMusicDetails)
                }
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    changeViewModelState(HomeViewModelSetStateAction.SetPlayState(isPlaying))
                }

                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    super.onShuffleModeEnabledChanged(shuffleModeEnabled)
                    changeViewModelState(HomeViewModelSetStateAction.SetShuffleState(shuffleModeEnabled))
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