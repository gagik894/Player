package com.example.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.player.presentation.mvi.HomeViewModel
import com.example.player.presentation.mvi.PlaybackIntent
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.MiniPlayer
import com.example.player.presentation.ui.screens.HomeScreen
import com.example.player.presentation.ui.screens.PlayerScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AppScreen {
    Home,
    Player
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    PlayerTheme {
        var currentScreen by remember { mutableStateOf(AppScreen.Home) }
        val playbackViewModel = remember { PlaybackViewModel() }
        val homeViewModel = remember { HomeViewModel() }
        val playbackViewState by playbackViewModel.viewState.collectAsStateWithLifecycle()
        
        Scaffold(
            bottomBar = {
                Column {
                    // Mini Player - only show when there's a current track and not on player screen
                    if (playbackViewState.playbackState.currentTrack != null && currentScreen != AppScreen.Player) {
                        MiniPlayer(
                            title = playbackViewState.playbackState.currentTrack?.title ?: "",
                            artist = playbackViewState.playbackState.currentTrack?.artist?.name ?: "",
                            isPlaying = playbackViewState.playbackState.isPlaying,
                            onPlayPauseClick = {
                                playbackViewModel.handleIntent(PlaybackIntent.PlayPause)
                            },
                            onNextClick = {
                                playbackViewModel.handleIntent(PlaybackIntent.SkipNext)
                            },
                            onExpandClick = {
                                currentScreen = AppScreen.Player
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            when (currentScreen) {
                AppScreen.Home -> {
                    HomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        homeViewModel = homeViewModel,
                        playbackViewModel = playbackViewModel
                    )
                }
                
                AppScreen.Player -> {
                    PlayerScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigateBack = { currentScreen = AppScreen.Home },
                        playbackViewModel = playbackViewModel
                    )
                }
            }
        }
    }
}