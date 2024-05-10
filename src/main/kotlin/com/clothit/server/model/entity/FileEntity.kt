package com.clothit.server.model.entity

import com.clothit.util.DateTimeUtil
import java.time.Instant


data class FileEntity(
    var id: Int?,
    var name: String,
    var size: Long,
    var item: ItemEntity?,
    var outfit : OutfitEntity?,
    var timeCreated: Instant,
    var timeUpdated: Instant
){

    constructor(name: String, size: Long):
            this(null, name,
                size, null, null,
                DateTimeUtil.getCurrentTime(),
                DateTimeUtil.getCurrentTime())

    constructor(id: Int? , name: String, size: Long):
            this(id, name,
                size, null, null,
                DateTimeUtil.getCurrentTime(),
                DateTimeUtil.getCurrentTime())
    constructor(name: String, size: Long, item: ItemEntity?):
            this(null, name,
                size, item, null,
                DateTimeUtil.getCurrentTime(),
                DateTimeUtil.getCurrentTime())

    constructor(id: Int, name: String, size: Long, item: ItemEntity?,timeCreated: Instant, timeUpdated: Instant):
            this(id, name,
                size, item, null,
                timeCreated,
                timeUpdated)


    constructor(name: String, size: Long, outfit: OutfitEntity?):
            this(null, name,
                size, null,outfit,
                DateTimeUtil.getCurrentTime(),
                DateTimeUtil.getCurrentTime())
}
