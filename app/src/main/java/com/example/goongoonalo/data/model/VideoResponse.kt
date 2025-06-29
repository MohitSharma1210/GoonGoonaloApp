package com.example.goongoonalo.data.model

data class VideoResponse(
    val TOP: List<VideoItem>,
    val New: List<VideoItem>,
    val Continue: List<VideoItem>
)