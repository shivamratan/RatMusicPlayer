package com.ratanapps.exoplayersample.ui.feature_home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ratanapps.exoplayersample.domain.model.Album
import com.ratanapps.exoplayersample.domain.model.Artist
import com.ratanapps.exoplayersample.domain.model.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onTrackClick: (Track) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.refreshTracks() },
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (uiState.continueListening != null) {
                    item {
                        SectionHeader("Continue Listening")
                        TrackItem(uiState.continueListening!!, onTrackClick)
                    }
                }

                item {
                    SectionHeader("Recently Played")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.recentlyPlayed) { track ->
                            MediaCard(track.title, track.artist, track.imageUrl)
                        }
                    }
                }

                item {
                    SectionHeader("Recommended Albums")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.recommendedAlbums) { album ->
                            MediaCard(album.title, album.artist, album.imageUrl)
                        }
                    }
                }

                item {
                    SectionHeader("Popular Artists")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.popularArtists) { artist ->
                            MediaCard(artist.name, "", artist.imageUrl)
                        }
                    }
                }
            }
            
            if (uiState.isLoading && uiState.recentlyPlayed.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun MediaCard(title: String, subtitle: String, imageUrl: String) {
    Card(modifier = Modifier.width(140.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.size(124.dp).padding(4.dp)) {
                Text("Img") // Placeholder for AsyncImage
            }
            Text(title, maxLines = 1, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            if (subtitle.isNotBlank()) {
                Text(subtitle, maxLines = 1, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
