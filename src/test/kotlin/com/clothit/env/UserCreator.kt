package com.clothit.env

import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.impl.TokenDaoImpl
import com.clothit.server.dao.impl.UserDaoImpl
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.service.impl.JwtServiceImpl
import com.clothit.server.service.impl.UserServiceImpl
import java.util.*

object UserCreator {

    val email = Random().nextInt(1000).toString() + "@example.com"
    val password = "password"
    val username = email.split("@")[0]

    val userDao = UserDaoImpl
    val tokenDao = TokenDaoImpl
    val jwtService = JwtServiceImpl(userDao, tokenDao)
    val userService = UserServiceImpl(userDao, jwtService)

    private fun registerUser(email: String, password: String, username: String): String {
        val userReq = UserRegisterReq(email, password, username)
        val userId = userService.registerUser(userReq)
        return userId
    }

    fun createUserTest(): UserTestObject {
        val token = registerUser(email, password, username)
        val userEntity = userDao.searchByEmail(email)!!
        return UserTestObject(
            userEntity = userEntity,
            userId = userEntity.id!!,
            userPassword = password
        )
    }

}

class UserTestObject(
    val userEntity: UserEntity,
    val userId : UUID,
    val userPassword: String
){

}