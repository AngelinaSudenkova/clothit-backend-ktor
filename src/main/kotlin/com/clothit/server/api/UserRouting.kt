package com.clothit.server.api

import com.clothit.server.api.req.UserLoginReq
import com.clothit.server.api.req.UserLogoutReq
import com.clothit.server.api.req.UserRegisterReq
import io.github.smiley4.ktorswaggerui.dsl.config.OpenApiInfo
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.userRoutingConfigure() {

    val userController: UserController by inject()

    routing {
        post("service/clothit/api/v1/login") {
            val req = call.receive<UserLoginReq>()
            val signInDto = userController.login(req)
            if (signInDto != null) {
                call.respond(signInDto)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }


        }
        post("service/clothit/api/v1/register") {

            val req = call.receive<UserRegisterReq>()
            val signUpDto = userController.register(req)
            if (signUpDto != null) {
                call.respond(signUpDto.toString())
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("service/clothit/api/v1/logout") {
            val req = call.receive<UserLogoutReq>()
            userController.logout(req.token)
            call.respond(HttpStatusCode.OK)
        }
    }
}