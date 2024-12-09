package com.clothit.server.api

import com.clothit.server.service.impl.JwtServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import java.util.*

fun Application.friendRoutingConfigure() {

    val friendController: FriendController by inject()
    val jwtService: JwtServiceImpl by inject()


    routing {
        authenticate {

            get("service/clothit/api/v1/friend/list") {
                val userId = call.principal<JWTPrincipal>()?.let { principal ->
                    val credential = JWTCredential(principal.payload)
                    jwtService.getUserIdFromToken(credential)
                }
                if(userId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    return@get
                }
                val friends = friendController.getAllFriends(userId)
                call.respond(friends)
            }


            get("service/clothit/api/v1/friend/pending") {
                val userId = call.principal<JWTPrincipal>()?.let { principal ->
                    val credential = JWTCredential(principal.payload)
                    jwtService.getUserIdFromToken(credential)
                }
                if(userId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    return@get
                }
                val pendingRequests = friendController.getPendingRequests(UUID.fromString(userId.toString()))
                call.respond(pendingRequests)
            }


            post("service/clothit/api/v1/friend/request/{recipientId}") {
                val senderId = call.principal<JWTPrincipal>()?.let { principal ->
                    val credential = JWTCredential(principal.payload)
                    jwtService.getUserIdFromToken(credential)
                }

                val recipientId = call.parameters["recipientId"]
                if (senderId == null || recipientId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    return@post
                }

                val friendDto = friendController.sendFriendRequest(senderId.toString(), recipientId)
                call.respond(HttpStatusCode.Created, friendDto)
            }


            post("service/clothit/api/v1/friend/accept/{requestId}") {
                val userId = call.principal<JWTPrincipal>()?.let { principal ->
                    val credential = JWTCredential(principal.payload)
                    jwtService.getUserIdFromToken(credential)
                }

                val requestId = call.parameters["requestId"]?.toIntOrNull()
                if (userId == null || requestId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    return@post
                }

                val success = friendController.acceptFriendRequest(requestId)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Friend request accepted")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Unable to accept request")
                }
            }


            post("service/clothit/api/v1/friend/reject/{requestId}") {
                val userId = call.principal<JWTPrincipal>()?.let { principal ->
                    val credential = JWTCredential(principal.payload)
                    jwtService.getUserIdFromToken(credential)
                }

                val requestId = call.parameters["requestId"]?.toIntOrNull()
                if (userId == null || requestId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    return@post
                }

                val success = friendController.rejectFriendRequest(requestId)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Friend request rejected")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Unable to reject request")
                }
            }


            delete("service/clothit/api/v1/friend/delete/{requestId}") {
                val userId = call.principal<JWTPrincipal>()?.let { principal ->
                    val credential = JWTCredential(principal.payload)
                    jwtService.getUserIdFromToken(credential)
                }

                val requestId = call.parameters["requestId"]?.toIntOrNull()
                if (userId == null || requestId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    return@delete
                }

                friendController.deleteFriendRequest(requestId)
                call.respond(HttpStatusCode.OK, "Friend request deleted")
            }
        }
    }
}
