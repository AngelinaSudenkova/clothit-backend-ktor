package com.clothit.server.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemShortDto(
    val id: Int?,
    val urlFile: String
){}
