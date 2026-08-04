package com.ratanapps.exoplayersample.ui.feature_library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratanapps.exoplayersample.domain.usecase.GetTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        observeLibraryItems()
    }

    private fun observeLibraryItems() {
        getTracksUseCase()
            .onEach { tracks ->
                _uiState.update { it.copy(tracks = tracks) }
            }
            .launchIn(viewModelScope)
    }

    fun onFilterSelected(filter: LibraryFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }
}