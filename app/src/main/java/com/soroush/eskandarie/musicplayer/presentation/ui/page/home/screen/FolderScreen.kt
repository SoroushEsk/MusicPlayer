package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction
import com.soroush.eskandarie.musicplayer.presentation.nav.Destination
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FolderPage(
    modifier: Modifier = Modifier,
    navigate: (action: NavControllerAction) -> Unit,
    getState: (action: HomeViewModelGetStateAction) -> StateFlow<*>
) {
    val folderList by
            (getState(HomeViewModelGetStateAction.GetFolderList) as StateFlow<Map<String, List<MusicFile>>>)
                .collectAsState()
    
    LazyColumn {
        items(folderList.keys.size) {
            Text(text = folderList.keys.toList()[it])
        }
    }
}