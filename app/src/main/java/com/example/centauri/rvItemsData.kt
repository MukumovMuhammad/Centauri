package com.example.centauri

import com.example.centauri.models.rvItemType

data class rvItemsData(
    val itemType: rvItemType,
    val number: Int,
    val title: String,
    val icon: Int,
    val numberOfParts: Int,
)
