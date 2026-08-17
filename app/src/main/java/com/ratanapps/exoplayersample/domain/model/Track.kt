package com.ratanapps.exoplayersample.domain.model

data class Track(
    val id: String,
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