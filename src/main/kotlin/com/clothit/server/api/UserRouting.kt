package com.clothit.server.api

import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserLogoutReq
import com.clothit.server.api.req.UserRegisterReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRoutingConfigure() {
    val userController = UserController()

    routing {
        post("service/clothit/api/v1/login") {
            val req = call.receive<UserLoginReq>()
            val token = userController.login(req)
            if (token != null) {
                call.respond(token.toString())
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }


        }
        post("service/clothit/api/v1/register") {
            val req = call.receive<UserRegisterReq>()
            val token = userController.register(req)
            if (token != null) {
                call.respond(token.toString())
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("service/clothit/api/v1/logout") {
            val req = call.receive<UserLogoutReq>()
            userController.logout(req.token)
            call.respond(HttpStatusCode.OK)
        }
    }
}