package com.example.player.presentation.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.player.presentation.ui.components.SearchBar
import com.example.player.presentation.ui.components.common.PlayerTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericListScreen(
    title: String,
    filteredItems: List<T>,
    searchQuery: String? = null,
    isLoading: Boolean,
    error: String? = null,
    searchPlaceholder: String? = null,
    emptyMessage: String,
    onSearchQueryChange: ((String) -> Unit)?,
    itemKey: (T) -> String,
    itemContent: @Composable (T) -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            PlayerTopAppBar(
                title = title,
                onBack = onBack
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding)
                ) {
                    if (onSearchQueryChange != null){
                        SearchBar(
                            query = searchQuery ?: "",
                            onQueryChange = onSearchQueryChange,
                            placeholder = searchPlaceholder ?: "Search...",
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

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
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        filteredItems.isEmpty() && !searchQuery.isNullOrEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No results found for \"$searchQuery\"",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        filteredItems.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = emptyMessage,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        else -> {
                            if (isPortrait) {
                                // Portrait: Single column list
                                LazyColumn(
                                    contentPadding = PaddingValues(bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(
                                        items = filteredItems,
                                        key = itemKey
                                    ) { item ->
                                        itemContent(item)
                                    }
                                }
                            } else {
                                // Landscape: Grid layout for better space utilization
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(minSize = 300.dp),
                                    contentPadding = PaddingValues(bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(
                                        items = filteredItems,
                                        key = itemKey
                                    ) { item ->
                                        itemContent(item)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}