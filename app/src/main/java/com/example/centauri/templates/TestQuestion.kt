package com.example.centauri.templates

import kotlinx.serialization.Serializable

@Serializable
data class TestQuestion(
    val question: String,
    val A: String,
    val B: String,
    val C: String,
    val D: String,
    val answer: Int
)
