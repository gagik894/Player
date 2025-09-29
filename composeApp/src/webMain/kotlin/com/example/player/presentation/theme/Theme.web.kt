package com.example.player.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun dynamicColorScheme(darkTheme: Boolean): ColorScheme? {
    // Dynamic colors are not supported on Web yet
    return null
}