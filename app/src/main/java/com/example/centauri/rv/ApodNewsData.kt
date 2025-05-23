package com.example.centauri.rv

import kotlinx.serialization.Serializable

@Serializable
data class ApodNewsData(
    var title: String = "",
    var date: String = "",
    var url: String = "",
    var explanation: String = ""
)
