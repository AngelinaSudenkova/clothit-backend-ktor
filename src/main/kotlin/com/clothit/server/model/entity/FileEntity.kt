package com.clothit.server.model.entity

import com.clothit.util.DateTimeUtil
import java.time.Instant


data class FileEntity(
    var id: Int?,
    var name: String,
    var size: Long,
    var item: ItemEntity?,
    var outfit: OutfitEntity?,
    var timeCreated: Instant,
    var timeUpdated: Instant
) {

    constructor(name: String, size: Long) :
            this(
                null, name,
                size, null, null,
                DateTimeUtil.getCurrentTime(),
                DateTimeUtil.getCurrentTime()
            )

    fun update (name: String, size: Long,  timeCreated: Instant,
                timeUpdated: Instant) {
        this.name = name
        this.size = size
        this.timeCreated = timeCreated
        this.timeUpdated = timeUpdated
    }

    companion object {
        fun filePreSaveInit(name: String, size: Long): FileEntity {
            return FileEntity(name, size)
        }
    }
}
