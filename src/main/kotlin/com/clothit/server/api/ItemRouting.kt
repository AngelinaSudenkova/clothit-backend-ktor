package com.clothit.server.api


import com.clothit.server.api.req.ItemCreateReq
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.itemRoutingConfigure() {
    val itemController = ItemController()
    routing {
        post("service/clothit/api/v1/item") {
            val req = call.receive<ItemCreateReq>()
            val idDto = itemController.save(req)
            call.respond(idDto.toString())
        }

        get("service/clothit/api/v1/item/list") {
            val listShortItems = itemController.get()
            call.respond(listShortItems.toString())
        }
    }
}