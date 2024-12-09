package com.clothit.server.api

import com.clothit.server.api.dto.FriendDto
import com.clothit.server.service.FriendService
import java.util.*

class FriendController(
    private val friendService: FriendService
) {

    fun getAllFriends(userId: UUID): List<FriendDto> {
        return friendService.getFriendsByUser(userId.toString())
    }


    fun getPendingRequests(userId: UUID): List<FriendDto> {
        return friendService.getPendingRequests(userId.toString())
    }


    fun sendFriendRequest(senderId: String, recipientId: String): FriendDto {
        return friendService.sendFriendRequest(senderId, recipientId)
    }


    fun acceptFriendRequest(requestId: Int): Boolean {
        return friendService.acceptFriendRequest(requestId)
    }


    fun rejectFriendRequest(requestId: Int): Boolean {
        return friendService.rejectFriendRequest(requestId)
    }


    fun deleteFriendRequest(requestId: Int) {
        friendService.delete(requestId)
    }
}
