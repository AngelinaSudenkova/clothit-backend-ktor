package com.clothit.server.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class SingInDto(
    val token: String
)
