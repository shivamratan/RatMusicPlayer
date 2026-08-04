package com.ratanapps.exoplayersample.data.remote

import retrofit2.http.GET

interface MusicApiService {
    @GET("tracks")
    suspend fun getTracks(): List<TrackDto>

    companion object {
        const val BASE_URL = "https://api.example.com/"
    }
}