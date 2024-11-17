package com.clothit.server.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenDto(
    val accessToken: String
)