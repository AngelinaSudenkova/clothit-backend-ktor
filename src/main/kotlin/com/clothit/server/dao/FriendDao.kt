package com.example.server.dao

import com.clothit.server.model.entity.FriendEntity
import com.clothit.server.model.enums.FriendApplicationStatus
import java.util.*

interface FriendDao {

    fun save(entity: FriendEntity): Int?

    fun getById(id: Int): FriendEntity?

    fun getAll(): List<FriendEntity>

    fun getFriendRequest(senderId: String, recipientId: String): FriendEntity?

    fun getFriendsByUser(userId: UUID): List<FriendEntity>

    fun getFriendsByStatus(userId: UUID, status: FriendApplicationStatus): List<FriendEntity>

    fun update(entity: FriendEntity)

    fun updateStatus(id: Int, status: FriendApplicationStatus)

    fun delete(id: Int)

    fun deleteSentRequest(senderId: String, recipientId: String)
}

