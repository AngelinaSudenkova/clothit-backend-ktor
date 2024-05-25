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

    fun register(req: UserRegisterReq): UUID? {
        val userId = userService.registerUser(req)
        return userId
    }

    fun login(req: UserLoginReq): UserDto? {
        val userDto = userService.authenticateUser(req.email, req.password)
        if(userDto != null){
            return userDto
        }
        return null
    }

    fun get(userId: UUID): UserDto? {
        val userDto = userService.getUser(userId)
        // TODO: Add authorization logic here
        return userDto
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