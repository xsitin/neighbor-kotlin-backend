package com.example.neighbor.dto

data class AdvertisementDto(
    val id: Long,
    val price: Int = 0,
    val ownerName: String,
    val title: String,
    val description: String,
    val imagesIds: Array<String>,
    val category: String
)