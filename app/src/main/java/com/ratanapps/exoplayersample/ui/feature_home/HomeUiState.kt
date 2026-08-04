package com.ratanapps.exoplayersample.ui.feature_home

import com.ratanapps.exoplayersample.domain.model.Album
import com.ratanapps.exoplayersample.domain.model.Artist
import com.ratanapps.exoplayersample.domain.model.Track

data class HomeUiState(
    val recentlyPlayed: List<Track> = emptyList(),
    val recommendedAlbums: List<Album> = emptyList(),
    val popularArtists: List<Artist> = emptyList(),
    val continueListening: Track? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)