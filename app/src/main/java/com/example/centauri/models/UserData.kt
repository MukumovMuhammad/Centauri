package com.example.centauri.models

// In UserData.kt


import android.os.Parcelable
import com.example.centauri.rv.ApodNewsData
import com.google.protobuf.Timestamp
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

// For JSON parsing with Kotlinx.serialization
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Date

@Parcelize
@Serializable
data class UserData(
    var username: String = "",
    var email: String = "",
    var rating: Int = 0,
    var AvarageMark: Float = 0.0f,

    @Transient

    var joined : Date? = null,

    @Transient
    var password: String = "",
    var PartCompleted: Int = 0,
    var apodNasaNews: ArrayList<ApodNewsData> = arrayListOf()
) : Parcelable


