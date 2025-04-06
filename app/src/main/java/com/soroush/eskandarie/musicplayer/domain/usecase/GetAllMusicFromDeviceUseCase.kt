package com.soroush.eskandarie.musicplayer.domain.usecase

import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.repository.DeviceMusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMusicFromDeviceUseCase @Inject constructor(
    private val  deviceMusicRepository: DeviceMusicRepository
){
    suspend operator fun invoke() : Flow<List<MusicFile>> {
        return deviceMusicRepository.getAllMusicFiles()
    }
}