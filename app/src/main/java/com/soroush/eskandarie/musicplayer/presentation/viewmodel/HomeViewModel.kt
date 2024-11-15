package com.soroush.eskandarie.musicplayer.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soroush.eskandarie.musicplayer.presentation.action.HomeAction
import com.soroush.eskandarie.musicplayer.presentation.state.SearchFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {
    //region Viewmodel States
    private val _searchBox = MutableStateFlow(
        SearchFieldState(
            searchText = "",
            isSearch = false
        )
    )
    val searchBox : StateFlow<SearchFieldState> get() = _searchBox
    //endregion
    //region Viewmodel Action Channels
    private val actionChannel = Channel<HomeAction> ( Channel.UNLIMITED )
    //endregion
    //region Main Methods
    init{
        handleActions()
    }
    fun getAction(action: HomeAction){
        viewModelScope.launch {
            actionChannel.send(action)
        }
    }
    private fun handleActions(){
        viewModelScope.launch {
            actionChannel.receiveAsFlow().collect{ action ->
                Log.e("viewmodel action", (action as HomeAction.SetSearchText).searchText)
                when( action ){
                    is HomeAction.SetSearchText -> setSearchText(action.searchText)
                }
            }
        }
    }
    private suspend fun setSearchText(searchText: String){
        _searchBox.value = _searchBox.value.copy(searchText = searchText)
    }
    //endregion
    //region Override Methods
    override fun onCleared() {
        super.onCleared()
        actionChannel.close()
    }
    //endregion
}