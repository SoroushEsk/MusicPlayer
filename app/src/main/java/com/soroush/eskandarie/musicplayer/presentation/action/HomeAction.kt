package com.soroush.eskandarie.musicplayer.presentation.action

sealed class HomeAction {
    data class SetSearchText(val searchText: String) : HomeAction()
}