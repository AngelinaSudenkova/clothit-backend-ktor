package com.clothit.server.api

import com.clothit.server.api.dto.SingInDto
import com.clothit.server.api.dto.SingUpDto
import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.dto.UserFriendDto
import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.service.JwtService
import com.clothit.server.service.UserService
import java.util.*

class UserController(
    private val userService: UserService,
    private val tokenService: JwtService
) {

    fun register(req: UserRegisterReq): SingUpDto {
        val token = userService.registerUser(req)
        return SingUpDto(token)
    }

    fun login(req: UserLoginReq): SingInDto {
        val token = userService.authenticateUser(req.email, req.password)
        return SingInDto(token)

    }

    fun get(userId: UUID): UserDto {
        val userEntity = userService.getUser(userId)
        // TODO: Add authorization logic here
        return userEntity.toDto()
    }

    fun logout(token: String) {
        tokenService.deleteToken(token)
    }

//    fun update(userId: UUID, req: UserUpdateReq) {
//        userService.updateUser(userId, req)
//    }

    fun delete(userId: UUID) {
        userService.deleteUser(userId)
    }

    fun searchByUsername(name: String): List<UserDto> {
        return userService.searchByUsername(name)
    }

    fun searchFriendsByUsername(userId: UUID, name: String): List<UserFriendDto> {
        return userService.searchFriendsByUsername(userId, name)
    }
}