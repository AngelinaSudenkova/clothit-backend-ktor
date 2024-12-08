package com.clothit.config


import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        post("/test"){
          //  val registerController = RegisterController()
         //   registerController.getTestUser(call)
        }
        route("api.json") {
            openApiSpec()
        }
        // Create a route for the swagger-ui using the openapi-spec at "/api.json".
        route("swagger") {
            swaggerUI("/api.json")
        }
    }
}
