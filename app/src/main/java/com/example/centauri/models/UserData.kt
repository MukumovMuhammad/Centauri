package com.example.centauri.models

import com.example.centauri.rv.ApodNewsData
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    var username: String = "",
    var email: String = "",
    var rating: Int = 0,
    var password: String = "",
    var testCompleted: Int = 0,
    var apodNasaNews: ArrayList<ApodNewsData> = arrayListOf()
) : java.io.Serializable
