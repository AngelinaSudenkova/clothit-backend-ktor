package com.clothit.config


import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Testing")
        }
        post("/test"){
          //  val registerController = RegisterController()
         //   registerController.getTestUser(call)
        }
    }
}
