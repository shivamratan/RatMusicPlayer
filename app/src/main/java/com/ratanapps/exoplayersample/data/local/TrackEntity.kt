package com.ratanapps.exoplayersample.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ratanapps.exoplayersample.domain.model.Track

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val mediaUrl: String,
    val imageUrl: String,
    val durationMs: Long
)

fun TrackEntity.toTrack(): Track {
    return Track(
        id = id,
        title = title,
        artist = artist,
        mediaUrl = mediaUrl,
        imageUrl = imageUrl,
        durationMs = durationMs
    )
}

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        title = title,
        artist = artist,
        mediaUrl = mediaUrl,
        imageUrl = imageUrl,
        durationMs = durationMs
    )
}