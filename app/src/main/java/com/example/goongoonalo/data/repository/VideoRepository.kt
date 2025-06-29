package com.example.goongoonalo.data.repository

import com.example.goongoonalo.data.network.ApiService
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun fetchVideos() = api.getVideos()
}