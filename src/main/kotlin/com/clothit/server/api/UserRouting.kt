package com.clothit.server.api

import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserLogoutReq
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.impl.TokenDaoImpl
import com.clothit.server.dao.impl.UserDaoImpl
import com.clothit.server.service.impl.JwtServiceImpl
import com.clothit.server.service.impl.UserServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRoutingConfigure(){
   val jwtService = JwtServiceImpl(userService = UserServiceImpl(userDao = UserDaoImpl), tokenDao = TokenDaoImpl)
    val userController = UserController(userService = UserServiceImpl(UserDaoImpl), tokenService = jwtService)
    routing{
        post("service/clothit/api/v1/login"){
            val req = call.receive<UserLoginReq>()
            val userDto = userController.login(req)
            if(userDto != null){
                jwtService.createToken(req)
            }

        }
        post("service/clothit/api/v1/register"){
            val req = call.receive<UserRegisterReq>()
            val userId = userController.register(req)
            if(userId!= null ) {
                val token: String? = jwtService.createToken(req)
                call.respond(hashMapOf("token" to token, "userId" to userId.toString()))
            }
            call.respond(HttpStatusCode.BadRequest)
        }

        post("service/clothit/api/v1/logout"){
            val req = call.receive<UserLogoutReq>()
            userController.logout(req.token)
            call.respond(HttpStatusCode.OK)
        }
    }
}