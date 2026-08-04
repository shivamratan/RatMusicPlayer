package com.ratanapps.exoplayersample.domain.usecase

import com.ratanapps.exoplayersample.domain.repository.MusicRepository
import com.ratanapps.exoplayersample.domain.util.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RefreshTracksUseCase @Inject constructor(
    private val repository: MusicRepository
) {
    operator fun invoke(): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.Loading())
        try {
            repository.refreshTracks()
            emit(ApiResponse.Success(Unit))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}