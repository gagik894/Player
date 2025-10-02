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
import androidx.navigation.toRoute
import com.example.player.di.RepositoryModule
import com.example.player.domain.usecase.*
import com.example.player.navigation.PlayerDestination
import com.example.player.presentation.mvi.HomeViewModel
import com.example.player.presentation.mvi.PlaybackViewModel
import com.example.player.presentation.mvi.QueueViewModel
import com.example.player.presentation.mvi.ArtistDetailsViewModel
import com.example.player.presentation.mvi.ArtistsViewModel
import com.example.player.presentation.mvi.PlaylistDetailsViewModel
import com.example.player.presentation.mvi.PlaylistsViewModel
import com.example.player.presentation.ui.screens.ArtistDetailsScreen
import com.example.player.presentation.ui.screens.ArtistsScreen
import com.example.player.presentation.ui.screens.HomeScreen
import com.example.player.presentation.ui.screens.PlayerScreen
import com.example.player.presentation.ui.screens.PlaylistDetailsScreen
import com.example.player.presentation.ui.screens.PlaylistsScreen
import com.example.player.presentation.theme.PlayerTheme
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

            composable<PlayerDestination.Artists> {
                val artistsViewModel = remember {
                    ArtistsViewModel()
                }
                ArtistsScreen(
                    viewModel = artistsViewModel,
                    onArtistClick = { artistId ->
                        navController.navigate(PlayerDestination.ArtistDetail(artistId))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable<PlayerDestination.Playlists> {
                val playlistsViewModel = remember {
                    PlaylistsViewModel()
                }
                PlaylistsScreen(
                    viewModel = playlistsViewModel,
                    onPlaylistClick = { playlistId ->
                        navController.navigate(PlayerDestination.PlaylistDetail(playlistId))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable<PlayerDestination.ArtistDetail> { backStackEntry ->
                val artistDetail = backStackEntry.toRoute<PlayerDestination.ArtistDetail>()
                val artistDetailsViewModel = remember(artistDetail.artistId) {
                    ArtistDetailsViewModel(
                        artistId = artistDetail.artistId,
                        getArtistByIdUseCase = GetArtistByIdUseCase(RepositoryModule.musicRepository),
                        getTracksByArtistUseCase = GetTracksByArtistUseCase(RepositoryModule.musicRepository)
                    )
                }
                
                ArtistDetailsScreen(
                    viewModel = artistDetailsViewModel,
                    onTrackClick = { trackId ->
                        // Handle track click - could navigate to player or start playing
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable<PlayerDestination.PlaylistDetail> { backStackEntry ->
                val playlistDetail = backStackEntry.toRoute<PlayerDestination.PlaylistDetail>()
                val playlistDetailsViewModel = remember(playlistDetail.playlistId) {
                    PlaylistDetailsViewModel(
                        playlistId = playlistDetail.playlistId,
                        getPlaylistByIdUseCase = GetPlaylistByIdUseCase(RepositoryModule.musicRepository),
                        getTracksByPlaylistUseCase = GetTracksByPlaylistUseCase(RepositoryModule.musicRepository)
                    )
                }
                
                PlaylistDetailsScreen(
                    viewModel = playlistDetailsViewModel,
                    onTrackClick = { trackId ->
                        // Handle track click - could navigate to player or start playing
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxSize()
                )

            }
        }
    }
}