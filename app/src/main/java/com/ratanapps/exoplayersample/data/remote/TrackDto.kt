package com.ratanapps.exoplayersample.data.remote

import com.google.gson.annotations.SerializedName
import com.ratanapps.exoplayersample.domain.model.Track

data class TrackDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("media_url") val mediaUrl: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("duration_ms") val durationMs: Long
)

fun TrackDto.toTrack(): Track {
    return Track(
        id = id,
        title = title,
        artist = artist,
        mediaUrl = mediaUrl,
        imageUrl = imageUrl,
        durationMs = durationMs
    )
}