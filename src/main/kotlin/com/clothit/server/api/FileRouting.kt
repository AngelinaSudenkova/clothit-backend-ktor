package com.clothit.server.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


object FileUrlConstant{
    const val fileViewUrl = "service/clothit/api/v1/file/{fileId}"
    const val fileUpdateUrl = "service/clothit/api/v1/file/update/{fileId}"
}

fun Application.fileRoutingConfigure() {

    val fileController : FileController by inject()

    routing {
        post("service/clothit/api/v1/file") {
            val multipart = call.receiveMultipart()
            val idDto = fileController.save(multipart)
            call.respond(idDto.toString())
        }

        get(FileUrlConstant.fileViewUrl) {
            val fileId = call.parameters["fileId"]?.toIntOrNull()
            if (fileId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid file ID")
                return@get
            }
            //TODO(CUSTOM EXCEPTION)
            val byteArray = fileController.getById(fileId)
            call.respond(byteArray)
        }

        put(FileUrlConstant.fileUpdateUrl) {
            val fileId = call.parameters["fileId"]?.toIntOrNull()
            val multipart = call.receiveMultipart()
            if (fileId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid file ID")
                return@put
            }
            fileController.update(fileId,multipart)
            call.respond(HttpStatusCode.OK)
        }
    }
}
