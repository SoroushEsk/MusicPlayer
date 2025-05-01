package com.soroush.eskandarie.musicplayer.presentation.ui.page.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.domain.model.MusicFile
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelGetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.action.NavControllerAction
import com.soroush.eskandarie.musicplayer.presentation.nav.Destination
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.util.Constants
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FolderPage(
    modifier: Modifier = Modifier,
    navigate: (action: NavControllerAction) -> Unit,
    getState: (action: HomeViewModelGetStateAction) -> StateFlow<*>,
    colortheme: ColorTheme = if(isSystemInDarkTheme()) DarkTheme else LightTheme
) {
    val folderList by
            (getState(HomeViewModelGetStateAction.GetFolderList) as StateFlow<Map<String, List<MusicFile>>>)
                .collectAsState()
    val folderNameList by remember {
        mutableStateOf(folderList.keys.toList())
    }
    val listLazyState = rememberLazyListState()
    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .background(colortheme.Background),
        state = listLazyState,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ){
        items(folderList.keys.size) {
            FolderItem(
                modifier = Modifier
                    .padding(horizontal = Dimens.Padding.FolderPagePadding)
                    .clickable {
                        navigate(
                            NavControllerAction.NavigateToFolderMusic(
                                Destination.FolderMusicScreen.route,
                                folderNameList[it]
                            )
                        )
                    },
                folderName = folderNameList[it],
                colortheme = colortheme,
                shape = RoundedCornerShape(Dimens.CornerRadius.FolderItem)
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Spacing.MusicBarMotionLayoutContainerHeight)
            )
        }
    }
}
@Composable
fun FolderItem(
    modifier: Modifier = Modifier,
    folderName: String,
    colortheme: ColorTheme,
    shape: Shape
){
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.Size.FolderPageFolderItemHieght)
//            .background(colortheme.Surface)
            .clip(shape = shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colortheme.LightShadow,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .border(
                width = Dimens.Size.FolderItemBorderWidth,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        colortheme.DarkShadow.copy(alpha = Dimens.Alpha.FolderItemBorderDark)
                    )
//                    start = Offset.Zero,
//                    end = Offset.Infinite
                ),
                shape = shape
            )
            ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .padding(Dimens.Padding.FolderPageFolderItemIconPadding),
            painter = painterResource(id = Constants.FolderPage.FolderItemIconResource),
            contentDescription = Constants.FolderPage.FolderItemIconDescription
        )
        Spacer(modifier = Modifier
            .fillMaxHeight()
            .width(Dimens.Spacing.FolderItemIconFolderName))
        Text(
            text = folderName,
            style = MaterialTheme.typography.bodyLarge,
            color = colortheme.Text
        )
    }
}