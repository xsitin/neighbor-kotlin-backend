package com.example.neighbor.dto

data class UserRegistrationDto(
    val login: String,
    val password: String,
    val name: String,
    val email: String,
    val phone: String,
)
