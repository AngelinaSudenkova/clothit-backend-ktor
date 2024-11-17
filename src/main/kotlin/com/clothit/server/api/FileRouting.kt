package com.clothit.server.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


object FileUrlConstant {
    const val FILE_VIEW_URL = "service/clothit/api/v1/file/{fileId}"
    const val FILE_UPDATE_URL = "service/clothit/api/v1/file/update/{fileId}"
    const val FILE_SAVE = "service/clothit/api/v1/file"
}

fun Application.fileRoutingConfigure() {

    val fileController: FileController by inject()

    routing {

        post(FileUrlConstant.FILE_SAVE) {
            val multipart = call.receiveMultipart()
            val idDto = fileController.save(multipart)
            call.respond(idDto.toString())
        }

        get(FileUrlConstant.FILE_VIEW_URL) {
            val fileId = call.parameters["fileId"]?.toIntOrNull()
            val byteArray = fileController.getById(fileId)
            call.respond(byteArray)
        }

        put(FileUrlConstant.FILE_UPDATE_URL) {
            val fileId = call.parameters["fileId"]?.toIntOrNull()
            val multipart = call.receiveMultipart()

            fileController.update(fileId, multipart)
            call.respond(HttpStatusCode.OK)
        }
    }
}
