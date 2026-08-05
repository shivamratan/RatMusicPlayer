package com.ratanapps.exoplayersample.ui.feature_library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ratanapps.exoplayersample.domain.model.Track
import com.ratanapps.exoplayersample.ui.feature_home.TrackItem

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onTrackClick: (Track) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScrollableTabRow(
            selectedTabIndex = when(uiState.selectedFilter) {
                LibraryFilter.Playlists -> 0
                LibraryFilter.Albums -> 1
                LibraryFilter.Downloads -> 2
                LibraryFilter.Favorites -> 3
            },
            edgePadding = 0.dp,
            divider = {}
        ) {
            val filters = listOf(LibraryFilter.Playlists, LibraryFilter.Albums, LibraryFilter.Downloads, LibraryFilter.Favorites)
            filters.forEachIndexed { index, filter ->
                Tab(
                    selected = uiState.selectedFilter == filter,
                    onClick = { viewModel.onFilterSelected(filter) },
                    text = { Text(filter.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(uiState.tracks) { track ->
                TrackItem(track = track, onTrackClick = onTrackClick)
            }
        }
    }
}