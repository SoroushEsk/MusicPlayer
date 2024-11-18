package com.soroush.eskandarie.musicplayer.presentation.action

sealed class HomeSetAction {
    data class SetSearchText(val searchText: String) : HomeSetAction()
}