package com.clothit.server.model.entity

import com.clothit.server.model.enums.ItemCategory
import com.clothit.util.DateTimeUtil
import java.time.Instant


data class ItemEntity(
    var id: Int?,
    var category: ItemCategory,
    var description: String?,
    val timeCreation: Instant,


    ) {
    constructor(category: ItemCategory, description: String) : this(
        null,
        category,
        description,
        DateTimeUtil.getCurrentTime()
    ) {
    }
}
