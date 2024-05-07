package com.clothit.server.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class OutfitShortDto (
    val id : Int?,
    val urlFile : List<String>
)