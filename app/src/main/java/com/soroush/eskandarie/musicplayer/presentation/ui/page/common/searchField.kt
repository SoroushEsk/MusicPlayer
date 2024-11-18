package com.soroush.eskandarie.musicplayer.presentation.ui.page.common

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.soroush.eskandarie.musicplayer.presentation.action.HomeSetAction
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.DarkTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.Dimens
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.LightTheme
import com.soroush.eskandarie.musicplayer.presentation.ui.theme.ColorTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    placeHolder: String = "Search",
    shape: RoundedCornerShape = RoundedCornerShape(Dimens.CornerRadius.AppTextFieldCornerRadius),
    themeColors: ColorTheme = if (isSystemInDarkTheme()) DarkTheme else LightTheme,
    modifier: Modifier = Modifier,
    setState: (HomeSetAction) -> Unit,
    getState: State<String>,
    onChange: (String) -> Unit
) {
    val searchText by getState

    Box(
        modifier = Modifier
            .clip(shape)
            .border(
                1.dp,
                themeColors.Text,
                shape = shape
            )
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText ->
                setState(HomeSetAction.SetSearchText(newText))
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = placeHolder,
                    style = textStyle
                )
            },
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
                focusedLabelColor = themeColors.FocusedField
            )
        )
    }
}