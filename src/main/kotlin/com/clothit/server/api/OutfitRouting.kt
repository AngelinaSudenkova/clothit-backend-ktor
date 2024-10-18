package com.clothit.server.api

import com.clothit.server.api.req.OutfitCreateReq
import com.clothit.server.api.req.OutfitFindReq
import com.clothit.server.api.req.OutfitUpdateReq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.outfitRoutingConfigure() {
    val outfitController: OutfitController by inject()
    routing {
        authenticate {
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

            put("service/clothit/api/v1/outfit/{outfitId}") {
                val outfitId = call.parameters["outfitId"]?.toIntOrNull()
                val req = call.receive<OutfitUpdateReq>()
                val shortOutfit = outfitController.update(outfitId!!, req)
                call.respond(HttpStatusCode.OK, "Updated")
                return@put
            }

            get("service/clothit/api/v1/outfit/find") {
                val outfitName = call.receive<OutfitFindReq>().name
                outfitName?.let {
                    val shortOutfit = outfitController.find(outfitName)
                    call.respond(shortOutfit.toString())
                }
            }
        }
    }
}