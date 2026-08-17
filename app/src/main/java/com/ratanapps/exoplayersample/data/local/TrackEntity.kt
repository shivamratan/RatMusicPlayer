package com.ratanapps.exoplayersample.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ratanapps.exoplayersample.domain.model.Track

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val albumArtist: String,
    val movie: String,
    val releaseYear: Int,
    val duration: Int,
    val durationFormatted: String,
    val genre: String,
    val language: String,
    val composer: String,
    val lyricist: String,
    val recordLabel: String,
    val mediaUrl: String,
    val imageUrl: String,
    val metadataSource: String
)

fun TrackEntity.toTrack(): Track {
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
        mediaUrl = mediaUrl,
        imageUrl = imageUrl,
        metadataSource = metadataSource
    )
}

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
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
        mediaUrl = mediaUrl,
        imageUrl = imageUrl,
        metadataSource = metadataSource
    )
}