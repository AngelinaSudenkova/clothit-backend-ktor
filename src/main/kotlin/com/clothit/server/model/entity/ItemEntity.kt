package com.clothit.server.model.entity

import com.clothit.server.api.FileUrlConstant
import com.clothit.server.api.dto.ItemShortDto
import com.clothit.server.api.req.ItemUpdateReq
import com.clothit.server.model.enums.ItemCategory
import com.clothit.util.DateTimeUtil
import com.clothit.util.ServerConstants
import java.time.Instant


data class ItemEntity(
    var id: Int?,
    var category: ItemCategory?,
    var description: String?,
    val timeCreation: Instant,

    ) {
    constructor(category: ItemCategory, description: String) : this(
        null,
        category,
        description,
        DateTimeUtil.getCurrentTime()
    )

    fun toItemShortDto(fileId: Int) : ItemShortDto{
        return ItemShortDto(this.id, ServerConstants.constantServerUrl
                + FileUrlConstant.fileViewUrl.replace("{fileId}",fileId.toString()))
    }

    fun update(req: ItemUpdateReq){
        category = req.category
        description = req.description
    }
}
