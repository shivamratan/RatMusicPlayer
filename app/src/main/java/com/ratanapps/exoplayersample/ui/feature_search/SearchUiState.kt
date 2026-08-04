package com.ratanapps.exoplayersample.ui.feature_search

import com.ratanapps.exoplayersample.domain.model.Category
import com.ratanapps.exoplayersample.domain.model.Track

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<Track> = emptyList(),
    val categories: List<Category> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isSearching: Boolean = false
)