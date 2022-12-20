package ru.neighbor.dto

data class UserFullDto(
    val id: String,
    val login: String,
    val email: String,
    val phone: String,
    val name: String,
    val avatarId: String,
    val description: String,
    val role: Int = 0
)