package com.soroush.eskandarie.musicplayer.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soroush.eskandarie.musicplayer.presentation.action.HomeGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeSetAction
import com.soroush.eskandarie.musicplayer.presentation.state.SearchFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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
    private val searchBox : StateFlow<SearchFieldState>
        get() = _searchBox
    //endregion
    //region Viewmodel Action Channels
    private val setActionChannel = Channel<HomeSetAction> ( Channel.UNLIMITED )
    //endregion
    //region Main Methods
    init{
        handleSetActions()
    }
    @Composable
    fun getHomeState(action: HomeGetStateAction) = when(action){
        is HomeGetStateAction.GetSearchTextState -> getSearchTextState()
    }
    fun getHomeSetAction(action: HomeSetAction){
        viewModelScope.launch {
            setActionChannel.send(action)
        }
    }
    private fun handleSetActions(){
        viewModelScope.launch {
            setActionChannel.receiveAsFlow().collect{ action ->
                Log.e("viewmodel action", (action as HomeSetAction.SetSearchText).searchText)
                when( action ){
                    is HomeSetAction.SetSearchText -> setSearchText(action.searchText)
                }
            }
        }
    }
    private suspend fun setSearchText(searchText: String){
        _searchBox.value = _searchBox.value.copy(searchText = searchText)
    }
    @Composable
    private fun getSearchTextState() =
        searchBox.map { searchEvent ->
            searchEvent.searchText
        }.collectAsState(initial = "")
    //endregion
    //region Override Methods
    override fun onCleared() {
        super.onCleared()
        setActionChannel.close()
    }
    //endregion
}