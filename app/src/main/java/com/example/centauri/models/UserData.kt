package com.example.centauri.models

// In UserData.kt


import android.os.Parcelable
import com.example.centauri.rv.ApodNewsData
import kotlinx.parcelize.Parcelize

// For JSON parsing with Kotlinx.serialization
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Parcelize
@Serializable
data class UserData(
    var username: String = "",
    var email: String = "",
    var rating: Int = 0,

    // This field will be ignored by Kotlinx.serialization (JSON)
    // but WILL be included in the Android Parcel
    @Transient
    var password: String = "",

    var testCompleted: Int = 0,
    var apodNasaNews: ArrayList<ApodNewsData> = arrayListOf()
) : Parcelable


