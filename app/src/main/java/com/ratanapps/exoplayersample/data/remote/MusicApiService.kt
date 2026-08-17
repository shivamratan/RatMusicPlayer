package com.ratanapps.exoplayersample.data.remote

import retrofit2.http.GET

interface MusicApiService {
    @GET("get-all-songs.json")
    suspend fun getTracks(): List<TrackDto>

    companion object {
        const val BASE_URL = "https://shivamratan.github.io/papp-api/hls-stream/"
    }
}