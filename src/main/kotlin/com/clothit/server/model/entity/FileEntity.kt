package com.clothit.server.model.entity

import com.clothit.util.DateTimeUtil
import java.time.Instant


data class FileEntity(
    var id: Int?,
    var name: String,
    var size: Long,
    var item: ItemEntity?,
    var timeCreated: Instant,
    var timeUpdated: Instant
){
    constructor(name: String, size: Long, item: ItemEntity?):
            this(null, name,
                size, item,
                DateTimeUtil.getCurrentTime(),
                DateTimeUtil.getCurrentTime())
}
