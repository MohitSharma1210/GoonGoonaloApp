package com.example.goongoonalo.data.network

import com.example.goongoonalo.data.model.VideoResponse
import retrofit2.http.GET

interface ApiService {
    @GET("exec")
    suspend fun getVideos(): VideoResponse
}