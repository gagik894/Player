package com.example.player

import androidx.compose.runtime.*
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.screens.PlayerScreen
import com.example.player.presentation.mvi.PlayerViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    PlayerTheme {
        PlayerScreen()
    }
}