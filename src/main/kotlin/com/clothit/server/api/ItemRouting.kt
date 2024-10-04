package com.clothit.server.api


import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.api.req.ItemUpdateReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.itemRoutingConfigure() {

    val itemController: ItemController by inject()

    routing {
        authenticate {
            post("service/clothit/api/v1/item") {
                val req = call.receive<ItemCreateReq>()
                val idDto = itemController.save(req)
                call.respond(idDto.toString())
            }

            put("service/clothit/api/v1/item/{itemId}") {
                val itemId = call.parameters["itemId"]?.toIntOrNull()
                val req = call.receive<ItemUpdateReq>()
                itemController.update(itemId!!, req)
                call.respond(HttpStatusCode.OK, "Updated")
            }

            get("service/clothit/api/v1/item/list") {
                val listShortItems = itemController.get()
                call.respond(listShortItems.toString())
            }

            get("service/clothit/api/v1/item/list/{category}") {
                val categoryName = call.parameters["category"]
                if (categoryName != null) {
                    val listShortItems = itemController.getByCategory(categoryName)
                    call.respond(listShortItems.toString())
                }
            }
        }
    }
}