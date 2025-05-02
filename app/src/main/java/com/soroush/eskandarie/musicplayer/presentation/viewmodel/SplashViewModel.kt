package com.soroush.eskandarie.musicplayer.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soroush.eskandarie.musicplayer.domain.usecase.GetAllMusicFromDatabasePagerUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.SavingDeviceMusicToLocalDBUseCase
import com.soroush.eskandarie.musicplayer.domain.usecase.playlist.CreateANewPlaylistUseCase
import com.soroush.eskandarie.musicplayer.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val saveDeviceMusicToLocalDB : SavingDeviceMusicToLocalDBUseCase,
    private val createANewPlaylistUseCase: CreateANewPlaylistUseCase,
    private val getAllMusicFromDatabasePagerUseCase: GetAllMusicFromDatabasePagerUseCase
): ViewModel() {
    fun firstTimeLaunchActions(){


        if(sharedPreferences.getBoolean(
            Constants.SharedPreference.IntroductionToken,
            true
        )){
            viewModelScope.launch {
                saveDeviceMusicToLocalDB()

            }
            with(sharedPreferences.edit()) {
                putBoolean(
                    Constants.SharedPreference.IntroductionToken,
                    false
                )
                apply()
            }
        }
    }
}