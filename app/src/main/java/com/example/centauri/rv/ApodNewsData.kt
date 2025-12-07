package com.example.centauri.rv

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class ApodNewsData(
    var title: String = "",
    var date: String = "",
    var url: String = "",
    var explanation: String = ""
): Parcelable
