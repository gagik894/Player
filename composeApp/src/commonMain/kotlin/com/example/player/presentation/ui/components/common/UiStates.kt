package com.example.player.presentation.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.player.presentation.theme.PlayerTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(error: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $error",
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No track selected")
    }
}


@Preview(showBackground = true)
@Composable
fun LoadingStatePreview() {
    PlayerTheme {
        LoadingState()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStatePreview() {
    PlayerTheme {
        ErrorState(error = "Failed to load track")
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview() {
    PlayerTheme {
        EmptyState()
    }
}