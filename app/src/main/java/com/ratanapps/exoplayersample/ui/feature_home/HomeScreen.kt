package com.ratanapps.exoplayersample.ui.feature_home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
                            MediaCard(
                                title = track.title,
                                subtitle = track.artist,
                                imageUrl = track.imageUrl,
                                onClick = { onTrackClick(track) }
                            )
                        }
                    }
                }

                item {
                    SectionHeader("Recommended Albums")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.recommendedAlbums) { album ->
                            MediaCard(
                                title = album.title,
                                subtitle = album.artist,
                                imageUrl = album.imageUrl
                            )
                        }
                    }
                }

                item {
                    SectionHeader("Popular Artists")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.popularArtists) { artist ->
                            MediaCard(
                                title = artist.name,
                                subtitle = "",
                                imageUrl = artist.imageUrl
                            )
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
fun MediaCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(140.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(124.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
