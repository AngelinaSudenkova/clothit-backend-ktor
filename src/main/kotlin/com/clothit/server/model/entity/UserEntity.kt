package com.clothit.server.model.entity


import java.time.Instant
import java.util.*

data class UserEntity(
    val id: UUID?,
    val email: String,
    val passwordHash: String,
    val username: String,
    val registeredDate: Instant,
    val lastLoginDate: Instant,
    val isActive: Boolean
)
