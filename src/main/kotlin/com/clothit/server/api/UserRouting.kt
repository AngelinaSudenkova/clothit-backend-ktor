package com.clothit.server.api

import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.impl.UserDaoImpl
import com.clothit.server.service.impl.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRoutingConfigure(){
    val userController = UserController(userService = UserServiceImpl(UserDaoImpl))
    routing{
        post("service/clothit/api/v1/login"){
            val req = call.receive<UserLoginReq>()
            val userId = userController.login(req)
        }
        post("service/clothit/api/v1/register"){
            val req = call.receive<UserRegisterReq>()
            val userId = userController.register(req)
            call.respond(userId.toString())
        }
    }
}