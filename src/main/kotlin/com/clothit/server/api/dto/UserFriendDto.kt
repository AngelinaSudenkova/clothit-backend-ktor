package com.clothit.server.api.dto

import com.clothit.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserFriendDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String,
    val status: String,
    val friendshipId: String
)