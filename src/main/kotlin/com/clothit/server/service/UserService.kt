package com.clothit.server.service

import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.model.entity.UserEntity
import java.util.*

interface UserService {
    fun registerUser(userRegisterReq: UserRegisterReq): UUID
    fun authenticateUser(email: String, password: String): UserDto
    fun getUser(userId: UUID): UserEntity
    fun getByEmail(email: String): UserEntity
    //  fun updateUser(userId: UUID, req: UserUpdateReq)
    fun deleteUser(userId: UUID)
    fun searchByUsername(name: String): List<UserDto>
}