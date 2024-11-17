package com.clothit.server.api

import com.clothit.server.api.dto.SingInDto
import com.clothit.server.api.dto.SingUpDto
import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.service.TokenService
import com.clothit.server.service.UserService
import java.util.*

class UserController(
    private val userService: UserService,
    private val tokenService: TokenService
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
        return userEntity.toDto()
    }

    fun logout(token: String) {
        tokenService.deleteToken(token)
    }

    fun delete(userId: UUID) {
        userService.deleteUser(userId)
    }

    fun searchByUsername(name: String?): List<UserDto> {
        return userService.searchByUsername(name)
    }
}