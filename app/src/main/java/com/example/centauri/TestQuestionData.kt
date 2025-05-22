package com.example.centauri

import kotlinx.serialization.Serializable

@Serializable
data class TestQuestionData(
    val feedback: String,
    val question: String,
    val A: String,
    val B: String,
    val C: String,
    val D: String,
    val answer: Int
)
