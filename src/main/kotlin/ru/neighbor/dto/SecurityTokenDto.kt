package ru.neighbor.dto

import java.time.LocalDateTime

data class SecurityTokenDto(
    val login: String,
    val role: String,
    val accessToken: String,
    val expiredAt: LocalDateTime
)