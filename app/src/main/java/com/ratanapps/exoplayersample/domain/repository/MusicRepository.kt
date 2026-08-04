package com.ratanapps.exoplayersample.domain.repository

import com.ratanapps.exoplayersample.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getTracks(): Flow<List<Track>>
    suspend fun refreshTracks()
}