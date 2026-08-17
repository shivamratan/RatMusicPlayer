package com.ratanapps.exoplayersample.data.remote

import com.google.gson.annotations.SerializedName
import com.ratanapps.exoplayersample.domain.model.Track

data class TrackDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("album") val album: String,
    @SerializedName("album_artist") val albumArtist: String,
    @SerializedName("movie") val movie: String,
    @SerializedName("release_year") val releaseYear: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("duration_formatted") val durationFormatted: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("language") val language: String,
    @SerializedName("composer") val composer: String,
    @SerializedName("lyricist") val lyricist: String,
    @SerializedName("record_label") val recordLabel: String,
    @SerializedName("hls_stream_url") val hlsStreamUrl: String,
    @SerializedName("cover_image_url") val coverImageUrl: String?,
    @SerializedName("cover_image_source") val coverImageSource: String,
    @SerializedName("metadata_source") val metadataSource: String
)

fun TrackDto.toTrack(): Track {
    return Track(
        id = id,
        title = title,
        artist = artist,
        album = album,
        albumArtist = albumArtist,
        movie = movie,
        releaseYear = releaseYear,
        duration = duration,
        durationFormatted = durationFormatted,
        genre = genre,
        language = language,
        composer = composer,
        lyricist = lyricist,
        recordLabel = recordLabel,
        mediaUrl = hlsStreamUrl,
        imageUrl = coverImageSource,
        metadataSource = metadataSource
    )
}