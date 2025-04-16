package com.soroush.eskandarie.musicplayer.presentation.ui.page.common

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.presentation.action.HomeViewModelSetStateAction
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    placeHolder: String = "Search",
    shape: RoundedCornerShape = RoundedCornerShape(Dimens.CornerRadius.AppTextField),
    themeColors: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    modifier: Modifier = Modifier,
    setState: (HomeViewModelSetStateAction) -> Unit,
    getState: State<String>,
    onChange: (String) -> Unit
) {
    val searchText by getState

    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> isFocused = true
                is FocusInteraction.Unfocus -> isFocused = false
            }
        }
    }
    Box(
        modifier = modifier
            .height(if (isFocused) 64.dp else 52.dp)
            .clip(shape)
            .border(
                1.dp,
                themeColors.LightShadow,
                shape = shape
            )
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText ->
                setState(HomeViewModelSetStateAction.SetStateSearchTextHome(newText))
//                searchText = newText
            },
            singleLine = true,
            modifier = modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            label = if (isFocused.not()) {
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Field Icon"
                        )
                        Text(
                            text = placeHolder,
                            style = textStyle
                        )
                    }
                }
            } else null,
            prefix = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Field Icon"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = themeColors.FocusedField,
                focusedIndicatorColor = themeColors.Primary,
                unfocusedIndicatorColor = themeColors.Surface,
                containerColor = Color.Transparent,
                focusedPrefixColor = themeColors.FocusedField,
                focusedLabelColor = themeColors.FocusedField,
                focusedTextColor = themeColors.Text
            ),
            interactionSource = interactionSource
        )
    }
}