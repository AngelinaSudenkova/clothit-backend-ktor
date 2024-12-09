package com.clothit.server.api.req

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestCreateReq(
    val userIdInvited: String
)