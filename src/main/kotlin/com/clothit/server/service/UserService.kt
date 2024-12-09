package com.clothit.server.service

import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.dto.UserFriendDto
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.model.entity.UserEntity
import java.util.*

interface UserService {
    fun registerUser(userRegisterReq: UserRegisterReq): String
    fun authenticateUser(email: String, password: String): String
    fun getUser(userId: UUID): UserEntity
    fun getByEmail(email: String): UserEntity
    //  fun updateUser(userId: UUID, req: UserUpdateReq)
    fun deleteUser(userId: UUID)
    fun searchByUsername(name: String): List<UserDto>
    fun searchFriendsByUsername(userId: UUID, name: String): List<UserFriendDto>
}