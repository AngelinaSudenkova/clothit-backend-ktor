package com.clothit.server.api

import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.service.JwtService
import com.clothit.server.service.UserService
import com.clothit.server.service.impl.JwtServiceImpl
import com.clothit.server.service.impl.UserServiceImpl
import java.util.*

class UserController(
    private val userService: UserService = UserServiceImpl(),
    private val tokenService: JwtService = JwtServiceImpl()
) {

    fun register(req: UserRegisterReq): String? {
        val userId = userService.registerUser(req)
        if(userId != null){
            val token = tokenService.createToken(req)
            return token
        }
        return null
    }

    fun login(req: UserLoginReq): String? {
        val userDto = userService.authenticateUser(req.email, req.password)
        if(userDto != null){
            val token = tokenService.createToken(req)
            return token
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