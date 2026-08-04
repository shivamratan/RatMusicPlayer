package com.ratanapps.exoplayersample.domain.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val mediaUrl: String,
    val imageUrl: String,
    val durationMs: Long
)