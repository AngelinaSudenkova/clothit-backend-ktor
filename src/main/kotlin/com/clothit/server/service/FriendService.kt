package com.clothit.server.service

import com.clothit.server.api.dto.FriendDto
import com.clothit.server.model.entity.FriendEntity
import com.clothit.server.model.enums.FriendApplicationStatus

interface FriendService {

    fun save(entity: FriendEntity): Int?

    fun getById(id: Int): FriendEntity?

    fun getAll(): List<FriendEntity>

    fun sendFriendRequest(senderId: String, recipientId: String): FriendDto

    fun getFriendsByUser(userId: String): List<FriendDto>

    fun getAcceptedFriends(userId: String): List<FriendDto>

    fun getPendingRequests(userId: String): List<FriendDto>

    fun acceptFriendRequest(requestId: Int): Boolean

    fun deleteFriend(id: Int)

    fun rejectFriendRequest(requestId: Int): Boolean

    fun update(entity: FriendEntity)

    fun updateStatus(id: Int, status: FriendApplicationStatus)

}
