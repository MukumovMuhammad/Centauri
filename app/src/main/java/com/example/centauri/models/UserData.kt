package com.example.centauri.models

data class UserData(
    val username: String,
    val email: String,
    val rating: Int = 0,
    val password: String,
    var testCompleted: Int = 0
)
