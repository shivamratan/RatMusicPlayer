package com.ratanapps.exoplayersample.domain.usecase

import com.ratanapps.exoplayersample.domain.model.Track
import com.ratanapps.exoplayersample.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val repository: MusicRepository
) {
    operator fun invoke(): Flow<List<Track>> {
        return repository.getTracks()
    }
}