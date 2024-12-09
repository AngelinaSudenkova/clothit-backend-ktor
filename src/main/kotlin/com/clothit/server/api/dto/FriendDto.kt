package com.clothit.server.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class FriendDto(
    val id: Int?,
    val userOwnerId: String,
    val userInvitedId: String,
    val userNameInvited: String,
    val status: String
)