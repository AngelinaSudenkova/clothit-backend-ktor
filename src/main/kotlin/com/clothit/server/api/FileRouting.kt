package com.clothit.server.api

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.fileRoutingConfigure() {
    val fileController = FileController()
    routing {
        post("service/clothit/api/v1/file") {
            val multipart = call.receiveMultipart()
            val idDto = fileController.save(multipart)
            call.respond(idDto.toString())
        }
    }
}