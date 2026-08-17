package com.ratanapps.exoplayersample.data.repository

import com.ratanapps.exoplayersample.data.local.TrackDao
import com.ratanapps.exoplayersample.data.local.toTrack
import com.ratanapps.exoplayersample.data.local.toTrackEntity
import com.ratanapps.exoplayersample.data.remote.MusicApiService
import com.ratanapps.exoplayersample.data.remote.toTrack
import com.ratanapps.exoplayersample.domain.model.Track
import com.ratanapps.exoplayersample.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val api: MusicApiService,
    private val dao: TrackDao
) : MusicRepository {

    override fun getTracks(): Flow<List<Track>> {
        return dao.getTracks().map { entities ->
            entities.map { it.toTrack() }
        }
    }

    override suspend fun refreshTracks() {
        try {
            val response = api.getTracks()
            println("DEBUG: getTracks Response: $response")
            val remoteTracks = response.map { it.toTrack() }
            if (remoteTracks.isNotEmpty()) {
                dao.clearTracks()
                dao.insertTracks(remoteTracks.map { it.toTrackEntity() })
            }
        } catch (e: Exception) {
            println("DEBUG: getTracks Error: ${e.message}")
            throw e
        }
    }
}