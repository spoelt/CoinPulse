package com.spoelt.coinpulse.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary =  Color.White,
    background = Color.Black,
    surface = DarkGray,
    onSurface = Color.White,
    errorContainer = PastelRed
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    background = Color.White,
    surface = Color.White,
    onSurface = Color.Black,
    errorContainer = Chili
)

@Composable
fun CoinPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}