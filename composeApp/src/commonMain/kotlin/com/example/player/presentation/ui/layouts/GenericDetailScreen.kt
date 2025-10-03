package com.example.player.presentation.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.player.domain.model.Track
import com.example.player.presentation.ui.components.common.PlayerTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDetailScreen(
    title: String,
    tracks: List<Track>,
    isLoading: Boolean,
    error: String? = null,
    onBackClick: (() -> Unit)?,
    trackContent: @Composable (Track) -> Unit,
    topBarActions: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    headerContent: @Composable (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            PlayerTopAppBar(
                title = title,
                onBack = onBackClick,
                actions = topBarActions
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            BoxWithConstraints {
                val isPortrait = maxHeight > maxWidth
                val horizontalPadding = if (isPortrait) 16.dp else 24.dp

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        error != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = error,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                            }
                        }

                        tracks.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No tracks found",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                            }
                        }

                        else -> {
                            LazyColumn(
                                contentPadding = PaddingValues(
                                    horizontal = horizontalPadding,
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Optional header content (artist info, playlist info, etc.)
                                headerContent?.let { content ->
                                    item {
                                        content()
                                    }
                                }

                                items(
                                    items = tracks,
                                    key = { it.id }
                                ) { track ->
                                    trackContent(track)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}