package com.ratanapps.exoplayersample.ui.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratanapps.exoplayersample.domain.model.Album
import com.ratanapps.exoplayersample.domain.model.Artist
import com.ratanapps.exoplayersample.domain.usecase.GetTracksUseCase
import com.ratanapps.exoplayersample.domain.usecase.RefreshTracksUseCase
import com.ratanapps.exoplayersample.domain.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase,
    private val refreshTracksUseCase: RefreshTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeTracks()
        refreshTracks()
    }

    private fun observeTracks() {
        getTracksUseCase()
            .onEach { tracks ->
                _uiState.update { it.copy(
                    recentlyPlayed = tracks.take(5),
                    recommendedAlbums = tracks.map { track -> 
                        Album(track.id, "Album ${track.title}", track.artist, track.imageUrl)
                    }.distinctBy { it.id }.take(10),
                    popularArtists = tracks.map { track ->
                        Artist(track.artist, track.artist, track.imageUrl)
                    }.distinctBy { it.id }.take(10),
                    continueListening = tracks.firstOrNull()
                ) }
            }
            .launchIn(viewModelScope)
    }

    fun refreshTracks() {
        viewModelScope.launch {
            refreshTracksUseCase().collect { result ->
                when (result) {
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    is ApiResponse.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                }
            }
        }
    }
}