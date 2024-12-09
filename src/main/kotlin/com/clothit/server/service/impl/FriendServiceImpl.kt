package com.clothit.server.service.impl

import com.clothit.error.ErrorCustomMessage
import com.clothit.error.ErrorTypes
import com.clothit.server.api.dto.FriendDto
import com.clothit.server.dao.UserDao
import com.clothit.server.model.entity.FriendEntity
import com.clothit.server.model.enums.FriendApplicationStatus
import com.clothit.server.service.FriendService
import com.clothit.util.DateTimeUtil
import com.example.server.dao.FriendDao
import java.util.*

class FriendServiceImpl(
    private val friendDao: FriendDao,
    private val userDao: UserDao
) : FriendService {

    override fun save(entity: FriendEntity): Int? {
        return friendDao.save(entity)
    }

    override fun getById(id: Int): FriendEntity? {
        val friend = friendDao.getById(id) ?: return null
        return friend.copy(
            userOwner = friend.userOwner,
            userInvited = friend.userInvited
        )
    }

    override fun getAll(): List<FriendEntity> {
        val friends = friendDao.getAll()
        return friends.map { friend ->
            friend.copy(
                userOwner = friend.userOwner,
                userInvited = friend.userInvited
            )
        }
    }


    override fun sendFriendRequest(senderId: String, recipientId: String): FriendDto {
        if (senderId == recipientId) {
            throw ErrorCustomMessage(ErrorTypes.INVALID_REQUEST).toException()
        }
        val sender = userDao.getById(UUID.fromString(senderId))
        val recipient = userDao.getById(UUID.fromString(recipientId))

        if (sender == null || recipient == null) {
            throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }

        val existingRequest = friendDao.getFriendRequest(senderId, recipientId)
        if (existingRequest != null) {
            throw ErrorCustomMessage(ErrorTypes.ALREADY_FRIENDS).toException()
        }

        val senderRequest = FriendEntity(
            id = 0,
            userOwner = sender.id!!,
            userInvited = recipient.id!!,
            invitedTime = DateTimeUtil.getCurrentTime(),
            status = FriendApplicationStatus.SENT
        )
        val recipientRequest = FriendEntity(
            id = 0,
            userOwner = recipient.id,
            userInvited = sender.id!!,
            invitedTime = DateTimeUtil.getCurrentTime(),
            status = FriendApplicationStatus.PENDING
        )

        friendDao.save(senderRequest)
        friendDao.save(recipientRequest)

        return FriendDto(
            id = senderRequest.id,
            userOwnerId = sender.id.toString(),
            userInvitedId = recipient.id.toString(),
            userNameInvited = recipient.username,
            status = FriendApplicationStatus.SENT.name
        )
    }


    override fun getFriendsByUser(userId: String): List<FriendDto> {
        val friends = friendDao.getFriendsByUser(UUID.fromString(userId))
        return friends.map { friend ->
            val friendEntity = userDao.getById(friend.userInvited)
            FriendDto(
                id = friend.id,
                userOwnerId = friend.userOwner.toString(),
                userInvitedId = friend.userInvited.toString(),
                userNameInvited = friendEntity!!.username,
                status = friend.status.toString()
            )
        }
    }


    override fun getAcceptedFriends(userId: String): List<FriendDto> {
        val acceptedFriends = friendDao.getFriendsByStatus(UUID.fromString(userId), FriendApplicationStatus.ACCEPTED)
        return acceptedFriends.map { friend ->
            val friendEntity = userDao.getById(friend.userInvited)
            FriendDto(
                id = friend.id,
                userOwnerId = friend.userOwner.toString(),
                userInvitedId = friend.userInvited.toString(),
                userNameInvited = friendEntity!!.username,
                status = friend.status.toString()
            )
        }
    }


    override fun getPendingRequests(userId: String): List<FriendDto> {
        val pendingRequests = friendDao.getFriendsByStatus(UUID.fromString(userId), FriendApplicationStatus.PENDING)
        return pendingRequests.map { friend ->
            val friendEntity = userDao.getById(friend.userInvited)
            FriendDto(
                id = friend.id,
                userOwnerId = friend.userOwner.toString(),
                userInvitedId = friend.userInvited.toString(),
                userNameInvited = friendEntity!!.username,
                status = friend.status.toString()
            )
        }
    }


    override fun acceptFriendRequest(requestId: Int): Boolean {
        val friendRequest = friendDao.getById(requestId) ?: return false
        if (friendRequest.status != FriendApplicationStatus.PENDING) return false

        friendDao.updateStatus(requestId, FriendApplicationStatus.ACCEPTED)

        val reciprocalRequest = FriendEntity(
            id = requestId,
            userOwner = friendRequest.userInvited,
            userInvited = friendRequest.userOwner,
            invitedTime = friendRequest.invitedTime,
            status = FriendApplicationStatus.ACCEPTED
        )
        friendDao.save(reciprocalRequest)
        friendDao.deleteSentRequest(friendRequest.userInvited.toString(), friendRequest.userOwner.toString())
        return true
    }

    override fun rejectFriendRequest(requestId: Int): Boolean {
        val friendRequest = friendDao.getById(requestId) ?: return false
        if (friendRequest.status != FriendApplicationStatus.PENDING) return false

        friendDao.delete(requestId)
        return true
    }

    override fun update(entity: FriendEntity) {
        friendDao.update(entity)
    }

    override fun updateStatus(id: Int, status: FriendApplicationStatus) {
        friendDao.updateStatus(id, status)
    }

    //delete from recepient side
    override fun delete(id: Int) {
        friendDao.delete(id)
    }
}
