package com.clothit.server.model.entity

import com.clothit.server.api.dto.FriendDto
import com.clothit.server.model.enums.FriendApplicationStatus
import java.time.Instant
import java.util.*

data class FriendEntity(
    val id: Int?,
    val userOwner: UUID,
    val userInvited: UUID,
    val invitedTime: Instant,
    val status: FriendApplicationStatus,
) {
    fun toFriendDto() : FriendDto {
        return FriendDto(
            id = id,
            userOwnerId = userOwner.toString(),
            userInvitedId = userInvited.toString(),
            userNameInvited = "",
            status = status.toString()
        )
    }
}