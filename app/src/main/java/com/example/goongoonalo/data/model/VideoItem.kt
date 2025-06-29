package com.example.goongoonalo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoItem(
    val Thumbnail: String,
    val Title: String,
    val Order: Int
)