package com.clothit.server.model.entity

import com.clothit.server.api.FileUrlConstant
import com.clothit.server.api.dto.OutfitShortDto
import com.clothit.server.api.req.OutfitCreateReq
import com.clothit.server.api.req.OutfitUpdateReq
import com.clothit.server.model.enums.OutfitSeason
import com.clothit.util.DateTimeUtil
import com.clothit.util.ServerConstants
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


    fun toShortOutfitDto(fileEntities: List<FileEntity>) : OutfitShortDto{
        val urls = ArrayList<String>()
        for(fileEntity in fileEntities){
           val url = ServerConstants.SERVER_URL + FileUrlConstant.FILE_VIEW_URL.replace("{fileId}", fileEntity.id.toString())
            urls.add(url)
        }

        return OutfitShortDto(this.id, urls)
    }

    fun update(req: OutfitUpdateReq){
        this.name = req.name
        this.season = req.season
        this.description = req.description
        this.timeUpdated = DateTimeUtil.getCurrentTime()
    }

    companion object {
        fun outfitCreate(req: OutfitCreateReq): OutfitEntity {
            return OutfitEntity(req.season, req.description, req.name)
        }
    }
}