package com.clothit.server.api

import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.service.JwtService
import com.clothit.server.service.UserService
import java.util.*

class UserController(
    private val userService: UserService,
    private val tokenService: JwtService
) {

    fun register(req: UserRegisterReq): String {
        val userId = userService.registerUser(req)
        val token = tokenService.createToken(req)
        return token
    }

    fun login(req: UserLoginReq): String {
        val userDto = userService.authenticateUser(req.email, req.password)
        val token = tokenService.createToken(req)
        return token

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
}