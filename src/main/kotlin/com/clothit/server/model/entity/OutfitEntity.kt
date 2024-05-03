package com.clothit.server.model.entity

import com.clothit.server.model.enums.OutfitSeason
import com.clothit.util.DateTimeUtil
import java.time.Instant

data class OutfitEntity(
    var  id: Int?,
    var  season: OutfitSeason,
    var  description: String,
    var  name: String,
    var  timeCreated: Instant,
    var  timeUpdated: Instant
){
    constructor(season: OutfitSeason, description: String, name: String):
        this(null, season, description, name,
            DateTimeUtil.getCurrentTime(),
            DateTimeUtil.getCurrentTime())
}