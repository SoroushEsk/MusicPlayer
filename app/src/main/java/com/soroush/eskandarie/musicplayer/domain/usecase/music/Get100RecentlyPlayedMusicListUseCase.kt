package com.soroush.eskandarie.musicplayer.domain.usecase.music

import androidx.sqlite.db.SimpleSQLiteQuery
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.domain.repository.MusicRepository
import com.soroush.eskandarie.musicplayer.util.Constants
import javax.inject.Inject

class Get100RecentlyPlayedMusicListUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(limitAmount: Int =100): List<MusicFile> {
        return musicRepository.getOrderedMusicList(
            SimpleSQLiteQuery(
                "SELECT * FROM ${Constants.Database.MusicTableName} ORDER BY ${Constants.Database.MusicDatePlayedColumn} DESC LIMIT ?",
                arrayOf(limitAmount)
            )
        )
    }

}