package com.ratanapps.exoplayersample.ui.feature_library

import com.ratanapps.exoplayersample.domain.model.Album
import com.ratanapps.exoplayersample.domain.model.Track

data class LibraryUiState(
    val selectedFilter: LibraryFilter = LibraryFilter.Playlists,
    val tracks: List<Track> = emptyList(),
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = false
)

sealed class LibraryFilter(val name: String) {
    object Playlists : LibraryFilter("Playlists")
    object Albums : LibraryFilter("Albums")
    object Downloads : LibraryFilter("Downloads")
    object Favorites : LibraryFilter("Favorites")
}