package com.example.centauri.models

import kotlinx.serialization.Serializable

@Serializable
data class ApodNewsData(
    val title: String,
    val date: String,
    val url: String,
    val explanation: String
)
