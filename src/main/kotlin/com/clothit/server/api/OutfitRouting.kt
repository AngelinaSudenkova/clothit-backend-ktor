package com.clothit.server.api

import com.clothit.server.api.req.OutfitCreateReq
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.outfitRoutingConfigure() {
    val outfitController = OutfitController()
    routing {
        post("service/clothit/api/v1/outfit") {
            val req = call.receive<OutfitCreateReq>()
            val idDto = outfitController.save(req)
            call.respond(idDto.toString())
        }

        get("service/clothit/api/v1/outfit/list") {
            val listShortOutfits = outfitController.get()
            call.respond(listShortOutfits.toString())
        }
        get("service/clothit/api/v1/outfit/{outfitId}") {
            val outfitId = call.parameters["outfitId"]?.toIntOrNull()
            val shortOutfit = outfitController.get(outfitId!!)
            call.respond(shortOutfit.toString())
        }
    }
}