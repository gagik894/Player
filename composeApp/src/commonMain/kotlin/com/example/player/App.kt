package com.example.player

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.player.navigation.PlayerDestination
import com.example.player.presentation.mvi.HomeViewModel
import com.example.player.presentation.mvi.PlaybackIntent
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.MiniPlayer
import com.example.player.presentation.ui.screens.HomeScreen
import com.example.player.presentation.ui.screens.PlayerScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    PlayerTheme {
        val navController = rememberNavController()
        val playbackViewModel = remember { PlaybackViewModel() }
        val homeViewModel = remember { HomeViewModel() }
        val playbackViewState by playbackViewModel.viewState.collectAsStateWithLifecycle()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                Column {
                    val isPlayerScreen = currentRoute == PlayerDestination.Player::class.qualifiedName
                    // Mini Player - only show when there's a current track and not on player screen
                    if (playbackViewState.playbackState.currentTrack != null && !isPlayerScreen) {
                        MiniPlayer(
                            title = playbackViewState.playbackState.currentTrack?.title ?: "",
                            artist = playbackViewState.playbackState.currentTrack?.artist?.name
                                ?: "",
                            isPlaying = playbackViewState.playbackState.isPlaying,
                            onPlayPauseClick = {
                                playbackViewModel.handleIntent(PlaybackIntent.PlayPause)
                            },
                            onNextClick = {
                                playbackViewModel.handleIntent(PlaybackIntent.SkipNext)
                            },
                            onExpandClick = {
                                navController.navigate(PlayerDestination.Player)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = PlayerDestination.Home
            ) {
                composable<PlayerDestination.Home> {
                    HomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        homeViewModel = homeViewModel,
                        playbackViewModel = playbackViewModel
                    )
                }

                composable<PlayerDestination.Player>(
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }
                ) {
                    PlayerScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        playbackViewModel = playbackViewModel
                    )
                }
            }
        }
    }
}