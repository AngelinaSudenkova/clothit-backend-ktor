package com.clothit.server.model.entity


import com.clothit.server.api.dto.UserDto
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
){
    fun toDto() : UserDto{
        return UserDto(
            id = this.id!!,
            username = this.username
        )
    }
}
