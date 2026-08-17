package com.ratanapps.exoplayersample.ui.feature_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.ratanapps.exoplayersample.domain.model.Track
import com.ratanapps.exoplayersample.service.RatMediaControllerManager
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
    private val controllerManager: RatMediaControllerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _uiState.update { it.copy(isPlaying = isPlaying) }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            _uiState.update { state ->
                state.copy(
                    playbackState = playbackState,
                    duration = controllerManager.controller?.duration?.takeIf { it != androidx.media3.common.C.TIME_UNSET } ?: state.duration
                )
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _uiState.update { it.copy(error = error.localizedMessage) }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val track = mediaItem?.let {
                Track(
                    id = it.mediaId,
                    title = it.mediaMetadata.title.toString(),
                    artist = it.mediaMetadata.artist.toString(),
                    album = it.mediaMetadata.albumTitle.toString(),
                    albumArtist = it.mediaMetadata.albumArtist.toString(),
                    movie = "",
                    releaseYear = 0,
                    duration = 0,
                    durationFormatted = "",
                    genre = it.mediaMetadata.genre.toString(),
                    language = "",
                    composer = it.mediaMetadata.composer.toString(),
                    lyricist = "",
                    recordLabel = "",
                    mediaUrl = "", // Not needed for UI display
                    imageUrl = it.mediaMetadata.artworkUri.toString(),
                    metadataSource = ""
                )
            }
            _uiState.update { state ->
                state.copy(
                    currentTrack = track,
                    duration = controllerManager.controller?.duration?.takeIf { it != androidx.media3.common.C.TIME_UNSET } ?: state.duration
                )
            }
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

    /** Starts periodic updates for the current playback position. */
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

    /** Sets the track as the current media item and starts playback. */
    fun playTrack(track: Track) {
        controllerManager.connect { controller ->
            val mimeType = if (track.mediaUrl.endsWith(".m3u8")) {
                MimeTypes.APPLICATION_M3U8
            } else {
                null
            }

            val mediaItem = MediaItem.Builder()
                .setMediaId(track.id)
                .setUri(track.mediaUrl)
                .apply {
                    if (mimeType != null) {
                        setMimeType(mimeType)
                    }
                }
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist)
                        .setAlbumTitle(track.album)
                        .setAlbumArtist(track.albumArtist)
                        .setGenre(track.genre)
                        .setComposer(track.composer)
                        .setArtworkUri(android.net.Uri.parse(track.imageUrl))
                        .build()
                )
                .build()
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()
        }
    }

    /** Toggles the play/pause state of the current playback. */
    fun togglePlayPause() {
        controllerManager.controller?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    /** Seeks to the specified position in the current track. */
    fun seekTo(position: Long) {
        controllerManager.controller?.seekTo(position)
    }

    /** Skips to the next media item in the playlist. */
    fun skipNext() {
        controllerManager.controller?.seekToNext()
    }

    /** Skips to the previous media item in the playlist. */
    fun skipPrevious() {
        controllerManager.controller?.seekToPrevious()
    }

    override fun onCleared() {
        controllerManager.controller?.removeListener(playerListener)
        controllerManager.release()
        super.onCleared()
    }
}