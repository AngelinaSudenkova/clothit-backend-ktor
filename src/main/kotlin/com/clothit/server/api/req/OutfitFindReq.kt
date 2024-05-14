package com.clothit.server.api.req

import kotlinx.serialization.Serializable

@Serializable
data class OutfitFindReq(
    val name: String
)
