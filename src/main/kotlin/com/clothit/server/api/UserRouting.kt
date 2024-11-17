package com.clothit.server.api

import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserLogoutReq
import com.clothit.server.api.req.UserRegisterReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.userRoutingConfigure() {

    val userController: UserController by inject()

    routing {

        post("service/clothit/api/v1/user/login") {
            val req = call.receive<UserLoginReq>()
            val signInDto = userController.login(req)
            call.respond(signInDto)

        }
        post("service/clothit/api/v1/user/register") {
            val req = call.receive<UserRegisterReq>()
            val signUpDto = userController.register(req)
            call.respond(signUpDto.toString())
        }

        delete("service/clothit/api/v1/user/logout") {
            val req = call.receive<UserLogoutReq>()
            userController.logout(req.token)
            call.respond(HttpStatusCode.OK)
        }

        get("service/clothit/api/v1/user/{username}/search") {
            val name = call.parameters["username"]
            val userList = userController.searchByUsername(name)
            call.respond(userList)
        }

    }
}