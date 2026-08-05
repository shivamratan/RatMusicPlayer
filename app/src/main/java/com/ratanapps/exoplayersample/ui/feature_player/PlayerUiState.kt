package com.ratanapps.exoplayersample.ui.feature_player

import com.ratanapps.exoplayersample.domain.model.Track

data class PlayerUiState(
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val playbackState: Int = 0, // Idle
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val bufferPosition: Long = 0,
    val error: String? = null
)