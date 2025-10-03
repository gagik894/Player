package com.example.player.presentation.ui.components.playerScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.player.presentation.theme.PlayerTheme
import com.example.player.presentation.util.toTimeString
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun ProgressSection(
    currentPosition: Float,
    onPositionChange: (Float) -> Unit,
    currentTime: String,
    totalDuration: Duration = Duration.ZERO,
    isPlaying: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragPosition by remember { mutableStateOf(currentPosition) }
    
    var optimisticPosition by remember { mutableStateOf(currentPosition) }
    
    val animatedPosition by animateFloatAsState(
        targetValue = optimisticPosition,
        animationSpec = tween(
            durationMillis = 250, // Match the update frequency
            easing = LinearEasing
        ),
        label = "progress_animation"
    )
    
    LaunchedEffect(currentPosition, isDragging) {
        if (!isDragging) {
            optimisticPosition = currentPosition
        }
    }
    
    LaunchedEffect(isPlaying, currentPosition, totalDuration) {
        while (isPlaying && !isDragging && totalDuration > Duration.ZERO) {
            delay(16)
            
            val positionIncrement = 16f / totalDuration.inWholeMilliseconds.toFloat()
            val predictedPosition = (optimisticPosition + positionIncrement).coerceIn(0f, 1f)
            
            optimisticPosition = predictedPosition
        }
    }
    
    val displayPosition = if (isDragging) dragPosition else animatedPosition
    
    val displayCurrentTime = if (isDragging && totalDuration > Duration.ZERO) {
        val dragDuration = totalDuration * dragPosition.toDouble()
        dragDuration.toTimeString()
    } else {
        currentTime
    }

    Column(modifier = modifier) {
        Slider(
            value = displayPosition,
            onValueChange = { newPosition ->
                if (!isDragging) {
                    isDragging = true
                }
                dragPosition = newPosition
                optimisticPosition = newPosition
            },
            onValueChangeFinished = {
                onPositionChange(dragPosition)
                isDragging = false
                optimisticPosition = dragPosition
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayCurrentTime,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = totalDuration.toTimeString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressSectionPreview() {
    PlayerTheme {
        ProgressSection(
            currentPosition = 0.45f,
            onPositionChange = {},
            currentTime = "2:15",
            totalDuration = 2.minutes + 30.seconds,
            isPlaying = true
        )
    }
}