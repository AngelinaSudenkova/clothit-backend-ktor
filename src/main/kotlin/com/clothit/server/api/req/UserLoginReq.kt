package com.clothit.server.api.req

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginReq(
    val email: String,
    val password: String
)