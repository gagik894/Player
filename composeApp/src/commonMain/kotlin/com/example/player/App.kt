package com.example.player

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.player.navigation.PlayerDestination
import com.example.player.presentation.mvi.HomeViewModel
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.mvi.QueueViewModel
import com.example.player.presentation.theme.PlayerTheme
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
        val queueViewModel = remember { QueueViewModel() }
        NavHost(
            navController = navController,
            startDestination = PlayerDestination.Home
        ) {
            composable<PlayerDestination.Home> {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    homeViewModel = homeViewModel,
                    playbackViewModel = playbackViewModel,
                    onNavigateTo = { destination ->
                        navController.navigate(destination)
                    },
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
                    queueViewModel = queueViewModel,
                    playbackViewModel = playbackViewModel
                )

            }
        }
    }
}