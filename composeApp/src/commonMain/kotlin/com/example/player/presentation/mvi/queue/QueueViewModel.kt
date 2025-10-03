package com.example.player.presentation.mvi.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.player.di.RepositoryModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {

    private val playerRepository = RepositoryModule.playerRepository

    private val _viewState = MutableStateFlow(QueueViewState())
    val viewState: StateFlow<QueueViewState> = _viewState.asStateFlow()

    init {
        observeQueue()
    }

    private fun observeQueue() {
        viewModelScope.launch {
            playerRepository.getPlaybackState()
                .collect { playbackState ->
                    _viewState.update {
                        it.copy(
                            queue = playbackState.queue,
                            currentTrack = playbackState.currentTrack
                        )
                    }
                }
        }
    }
}