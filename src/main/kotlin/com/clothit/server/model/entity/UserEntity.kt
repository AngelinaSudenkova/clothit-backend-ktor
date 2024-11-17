package com.clothit.server.model.entity


import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.util.DateTimeUtil
import com.clothit.util.PasswordUtil
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
) {

    fun toDto(): UserDto {
        return UserDto(
            id = this.id!!,
            username = this.username
        )
    }


    companion object {
        fun of(req: UserRegisterReq): UserEntity {
            val hashedPassword = PasswordUtil.hashPassword(req.password)
            return UserEntity(
                id = null,
                email = req.email,
                passwordHash = hashedPassword,
                username = req.username,
                registeredDate = DateTimeUtil.getCurrentTime(),
                lastLoginDate = DateTimeUtil.getCurrentTime(),
                isActive = true
            )
        }

    }
}
