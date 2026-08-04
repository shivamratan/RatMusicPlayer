package com.ratanapps.exoplayersample.ui.feature_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratanapps.exoplayersample.domain.model.Category
import com.ratanapps.exoplayersample.domain.usecase.GetTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        // Initialize dummy categories
        _uiState.update { it.copy(
            categories = listOf(
                Category("1", "Pop", ""),
                Category("2", "Rock", ""),
                Category("3", "Hip Hop", ""),
                Category("4", "Jazz", "")
            ),
            recentSearches = listOf("Artist X", "Song Y")
        ) }

        observeSearchResults()
    }

    private fun observeSearchResults() {
        combine(getTracksUseCase(), _searchQuery) { tracks, query ->
            if (query.isBlank()) {
                _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            } else {
                val results = tracks.filter { 
                    it.title.contains(query, ignoreCase = true) || 
                    it.artist.contains(query, ignoreCase = true) 
                }
                _uiState.update { it.copy(searchResults = results, isSearching = true) }
            }
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        _uiState.update { it.copy(searchQuery = newQuery) }
    }
}