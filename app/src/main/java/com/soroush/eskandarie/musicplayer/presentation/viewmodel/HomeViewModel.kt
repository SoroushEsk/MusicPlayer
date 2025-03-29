package com.soroush.eskandarie.musicplayer.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaSession
import com.soroush.eskandarie.musicplayer.presentation.action.HomeGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeSetAction
import com.soroush.eskandarie.musicplayer.presentation.state.HomeViewModelState
import com.soroush.eskandarie.musicplayer.presentation.state.SearchFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaSession: MediaSession
): ViewModel() {
    //region Viewmodel States
    private val _homeState = MutableStateFlow(
        HomeViewModelState(
            SearchFieldState(
                searchText = "",
                isSearch = false
            )
        )
    )
     val homeState : StateFlow<HomeViewModelState>
        get() = _homeState

    private val _songPercent = MutableStateFlow(0.0f)
    val songPercent: StateFlow<Float> = _songPercent.asStateFlow()
    //endregion
    //region Viewmodel Action Channels
    private val setActionChannel = Channel<HomeSetAction> ( Channel.UNLIMITED )
    //endregion
    //region Main Methods
    init{
        handleSetActions()
    }
    fun getHomeSetAction(action: HomeSetAction){
        viewModelScope.launch {
            setActionChannel.send(action)
        }
    }
    private fun handleSetActions(){
        viewModelScope.launch {
            setActionChannel.receiveAsFlow().collect{ action ->
//                Log.e("viewmodel action", (action as HomeSetAction.SetSearchText).searchText)
                when( action ){
                    is HomeSetAction.SetSearchText -> setSearchText(action.searchText)
                }
            }
        }
    }
    private suspend fun setSearchText(searchText: String){
        _homeState.value.searchFieldState = _homeState.value.searchFieldState.copy(searchText = searchText)
    }
    fun setSongPercent(){
        val currentDuration = mediaSession.player.currentPosition.toFloat()
        val totalDuration = mediaSession.player.contentDuration
        updateSongPercent(currentDuration / totalDuration)
    }
    private fun updateSongPercent(newPercent: Float) {
        _songPercent.value = newPercent
        Log.e("percent", newPercent.toString())
    }
    //endregion
    //region Override Methods
    override fun onCleared() {
        super.onCleared()
        setActionChannel.close()
    }
    //endregion
}