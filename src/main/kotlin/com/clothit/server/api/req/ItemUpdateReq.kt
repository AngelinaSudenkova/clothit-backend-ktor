package com.clothit.server.api.req

import com.clothit.server.model.enums.ItemCategory
import kotlinx.serialization.Serializable

@Serializable
data class ItemUpdateReq(
    val category: ItemCategory,
    val description: String,
    val fileId: Int
)