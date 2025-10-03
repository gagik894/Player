package com.example.player.presentation.mvi.home

import com.example.player.domain.model.Track

/**
 * Represents the state of the home screen.
 *
 * @property isLoading Indicates whether the screen is currently loading data.
 * @property error An optional error message to be displayed on the screen.
 * @property tracks The list of all available tracks.
 * @property favorites The list of tracks marked as favorites by the user.
 * @property searchQuery The current search query entered by the user.
 * @property filteredTracks The list of tracks filtered based on the search query.
 */
data class HomeViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val tracks: List<Track> = emptyList(),
    val favorites: List<Track> = emptyList(),
    val searchQuery: String = "",
    val filteredTracks: List<Track> = emptyList()
) {
    companion object {
        val Empty = HomeViewState()
        val Loading = HomeViewState(isLoading = true)
        val Error = HomeViewState(error = "An error occurred")
        val sample = HomeViewState(
            isLoading = false,
            error = null,
            tracks = listOf(
                Track.sample,
                Track.sample2,
                Track.sample3
            ),
            favorites = listOf(
                Track.sample3
            ),
            searchQuery = "",
            filteredTracks = emptyList()
        )
    }
}