package com.clothit.server.api.req

import com.clothit.server.model.enums.OutfitSeason
import kotlinx.serialization.Serializable

@Serializable
data class OutfitUpdateReq(
    val season: OutfitSeason,
    val description: String,
    val name: String,
    val itemsId: List<Int>
)