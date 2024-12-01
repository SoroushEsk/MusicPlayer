package com.soroush.eskandarie.musicplayer.presentation.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

interface ColorTheme{
    val Background      : Color
    val Surface         : Color
    val Primary         : Color
    val Secondary       : Color
    val LightShadow     : Color
    val DarkShadow      : Color
    val Text            : Color
    val Tint            : Color
    val DarkSurface     : Color
    val Icon            : Color
    val FocusedField    : Color
}
object LightTheme : ColorTheme {
    override val Background  = Color(0xFFE0E0E0)
    override val Surface     = Color(0xFFFAFAFA)
    override val Primary     = Color(0xFF3A7CA5)
    override val Secondary   = Color(0xFF496E96)
    override val LightShadow = Color(0x4DFFFFFF)
    override val DarkShadow  = Color(0xCC5F6268)
    override val Text        = Color(0xEB181818)
    override val Tint        = Color(0xCC333333)
    override val DarkSurface = Color(0x1EC5C5C5)
    override val Icon        = Color(0xFF25344D)
    override val FocusedField= Color(0xFF284A81)
}
object DarkTheme : ColorTheme {
    override val Background  = Color(0xFF191919)
    override val Surface     = Color(0xFF1E1E1E)
    override val Primary     = Color(0xFF4285F4)
    override val Secondary   = Color(0xFF182C42)
    override val LightShadow = Color(0xFF3A3939)
    override val DarkShadow  = Color(0xFF000000)
    override val Text        = Color(0xE6FFFFFF)
    override val Tint        = Color(0x99E0E0E0)
    override val DarkSurface = Color(0xFA181818)
    override val Icon        = Color(0xFF4984E2)
    override val FocusedField= Color(0xFF689DF7)
}