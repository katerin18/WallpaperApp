package com.example.wallpaperapp.retrofit

data class DataResponseModel(
    val data: List<ImageResponseData>
)

data class ImageResponseData(
    val id: String,
    val url: String,
    val short_url: String,
    val views: Int,
    val favorites: Int,
    val source: String,
    val purity: String,
    val category: String,
    val dimension_x: Int,
    val dimension_y: Int,
    val resolution: String,
    val ratio: String,
    val file_size: Long,
    val file_type: String,
    val created_at: String,
    val colors: List<String>,
    val path: String,
    val thumbs: ThumbsData
)

data class ThumbsData(
    val large: String,
    val original: String,
    val small: String
)