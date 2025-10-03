package com.example.player

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
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
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.player.di.RepositoryModule
import com.example.player.domain.usecase.GetAlbumsByArtistUseCase
import com.example.player.domain.usecase.GetAlbumsById
import com.example.player.domain.usecase.GetArtistByIdUseCase
import com.example.player.domain.usecase.GetPlaylistByIdUseCase
import com.example.player.domain.usecase.GetTracksByAlbumUseCase
import com.example.player.domain.usecase.GetTracksByArtistUseCase
import com.example.player.domain.usecase.GetTracksByPlaylistUseCase
import com.example.player.navigation.PlayerDestination
import com.example.player.presentation.mvi.album.AlbumDetailsViewModel
import com.example.player.presentation.mvi.artistDetails.ArtistDetailsViewModel
import com.example.player.presentation.mvi.artists.ArtistsViewModel
import com.example.player.presentation.mvi.home.HomeViewModel
import com.example.player.presentation.mvi.playListDetails.PlaylistDetailsViewModel
import com.example.player.presentation.mvi.playLists.PlaylistsViewModel
import com.example.player.presentation.mvi.playback.PlaybackIntent
import com.example.player.presentation.mvi.playback.PlaybackViewModel
import com.example.player.presentation.mvi.queue.QueueViewModel
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.ui.components.MiniPlayer
import com.example.player.presentation.ui.screens.AlbumDetailsScreen
import com.example.player.presentation.ui.screens.ArtistDetailsScreen
import com.example.player.presentation.ui.screens.ArtistsScreen
import com.example.player.presentation.ui.screens.HomeScreen
import com.example.player.presentation.ui.screens.PlayerScreen
import com.example.player.presentation.ui.screens.PlaylistDetailsScreen
import com.example.player.presentation.ui.screens.PlaylistsScreen
import okio.FileSystem
import org.jetbrains.compose.ui.tooling.preview.Preview

//hardcoded because of issue with navigation in multiplatform project (class.qualifiedName)
private const val PLAYER_ROUTE = "com.example.player.navigation.PlayerDestination.Player"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    PlayerTheme {

        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        val navController = rememberNavController()
        val playbackViewModel = remember { PlaybackViewModel() }
        val homeViewModel = remember { HomeViewModel() }
        val queueViewModel = remember { QueueViewModel() }

        val currentDestination by navController.currentBackStackEntryAsState()
        val currentRoute = currentDestination?.destination?.route
        val playbackViewState by playbackViewModel.viewState.collectAsStateWithLifecycle()
        val shouldShowBottomBar =
            currentRoute != PLAYER_ROUTE && playbackViewState.playbackState.currentTrack != null
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (shouldShowBottomBar) {
                    MiniPlayer(
                        title = playbackViewState.playbackState.currentTrack?.title ?: "",
                        artistName = playbackViewState.playbackState.currentTrack?.artist?.name
                            ?: "Unknown Artist",
                        artworkUrl = playbackViewState.playbackState.currentTrack?.artworkUrl,
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
                        modifier = Modifier
                            .imePadding()
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = PlayerDestination.Home,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        if (shouldShowBottomBar) PaddingValues(bottom = 80.dp) else PaddingValues(
                            0.dp
                        )
                    )
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
                            navController.navigateUp()
                        },
                        queueViewModel = queueViewModel,
                        playbackViewModel = playbackViewModel,
                        onAlbumClick = { albumId ->
                            navController.navigate(PlayerDestination.AlbumDetail(albumId))
                        },
                        onArtistClick = { artistId ->
                            navController.navigate(PlayerDestination.ArtistDetail(artistId))
                        }
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
                        onNavigateBack = {
                            navController.navigateUp()
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
                        onNavigateBack = {
                            navController.navigateUp()
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
                            getAlbumsByArtistUseCase = GetAlbumsByArtistUseCase(RepositoryModule.musicRepository),
                            getTracksByArtistUseCase = GetTracksByArtistUseCase(RepositoryModule.musicRepository)
                        )
                    }

                    ArtistDetailsScreen(
                        viewModel = artistDetailsViewModel,
                        playbackViewModel = playbackViewModel,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onAlbumClick = { album ->
                            navController.navigate(PlayerDestination.AlbumDetail(album.id))
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
                        playbackViewModel = playbackViewModel,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                }

                composable<PlayerDestination.AlbumDetail> { backStackEntry ->
                    val albumDetail = backStackEntry.toRoute<PlayerDestination.AlbumDetail>()
                    val albumDetailsViewModel = remember(albumDetail.albumId) {
                        AlbumDetailsViewModel(
                            albumId = albumDetail.albumId,
                            getAlbumByIdUseCase = GetAlbumsById(RepositoryModule.musicRepository),
                            getTracksByAlbumUseCase = GetTracksByAlbumUseCase(RepositoryModule.musicRepository)
                        )
                    }

                    AlbumDetailsScreen(
                        viewModel = albumDetailsViewModel,
                        playbackViewModel = playbackViewModel,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

fun getAsyncImageLoader(context: coil3.PlatformContext) =
    ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.crossfade(true).logger(DebugLogger()).build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024) // 512MB
        .build()
}