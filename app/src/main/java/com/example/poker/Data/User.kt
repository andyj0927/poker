package com.example.poker.Data

import java.util.*

data class User(
    val id: Int,
    val userName: String,
    val displayName: String,
    val friends: String,
    val currentlyActive: Int,
    val createdAt: Date,
    val updatedAt: Date
)

data class UserRegisterRequest(
    val userName: String,
    val displayName: String,
    val password: String
    )

data class UserLoginRequest(
    val userName: String,
    val password: String
    )