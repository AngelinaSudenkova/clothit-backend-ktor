package com.clothit.server.api.req

import kotlinx.serialization.Serializable

@Serializable
data class UserLogoutReq(
    val token: String
)