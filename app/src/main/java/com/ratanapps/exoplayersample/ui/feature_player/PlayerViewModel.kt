package com.ratanapps.exoplayersample.ui.feature_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.ratanapps.exoplayersample.domain.model.Track
import com.ratanapps.exoplayersample.service.MediaControllerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val controllerManager: MediaControllerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _uiState.update { it.copy(isPlaying = isPlaying) }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            _uiState.update { it.copy(playbackState = playbackState) }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val track = mediaItem?.let {
                Track(
                    id = it.mediaId,
                    title = it.mediaMetadata.title.toString(),
                    artist = it.mediaMetadata.artist.toString(),
                    mediaUrl = "", // Not needed for UI display
                    imageUrl = it.mediaMetadata.artworkUri.toString(),
                    durationMs = 0 // Will get from player duration
                )
            }
            _uiState.update { it.copy(currentTrack = track, duration = controllerManager.controller?.duration ?: 0) }
        }
    }

    init {
        controllerManager.connect { controller ->
            controller.addListener(playerListener)
            _uiState.update { 
                it.copy(
                    isPlaying = controller.isPlaying,
                    playbackState = controller.playbackState,
                    duration = controller.duration
                )
            }
            startPositionUpdates()
        }
    }

    private fun startPositionUpdates() {
        viewModelScope.launch {
            while (isActive) {
                controllerManager.controller?.let { controller ->
                    _uiState.update { it.copy(
                        currentPosition = controller.currentPosition,
                        bufferPosition = controller.bufferedPosition
                    ) }
                }
                delay(1000)
            }
        }
    }

    fun playTrack(track: Track) {
        controllerManager.connect { controller ->
            val mediaItem = MediaItem.Builder()
                .setMediaId(track.id)
                .setUri(track.mediaUrl)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist)
                        .setArtworkUri(android.net.Uri.parse(track.imageUrl))
                        .build()
                )
                .build()
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()
        }
    }

    fun togglePlayPause() {
        controllerManager.controller?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun seekTo(position: Long) {
        controllerManager.controller?.seekTo(position)
    }

    fun skipNext() {
        controllerManager.controller?.seekToNext()
    }

    fun skipPrevious() {
        controllerManager.controller?.seekToPrevious()
    }

    override fun onCleared() {
        controllerManager.controller?.removeListener(playerListener)
        controllerManager.release()
        super.onCleared()
    }
}