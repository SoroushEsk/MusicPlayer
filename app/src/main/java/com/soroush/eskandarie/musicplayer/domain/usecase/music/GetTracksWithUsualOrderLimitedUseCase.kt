package com.soroush.eskandarie.musicplayer.domain.usecase.music

import androidx.sqlite.db.SimpleSQLiteQuery
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import com.soroush.eskandarie.musicplayer.util.Constants
import javax.inject.Inject

class GetTracksWithUsualOrderLimitedUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(limitAmount: Int) =
        musicRepository.getOrderedMusicList(
            SimpleSQLiteQuery(
                "SELECT * FROM ${Constants.Database.MusicTableName} ORDER BY ${Constants.Database.MusicIdColumn} DESC LIMIT ?",
                arrayOf(limitAmount)
            )
        )
}