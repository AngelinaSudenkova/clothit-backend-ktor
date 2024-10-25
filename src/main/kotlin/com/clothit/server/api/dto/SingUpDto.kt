package com.clothit.server.api.dto

import kotlinx.serialization.Serializable


@Serializable
data class SingUpDto(
    val token: String
)