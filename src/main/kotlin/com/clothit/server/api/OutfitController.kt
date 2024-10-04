package com.clothit.server.api

import com.clothit.server.api.dto.IdDto
import com.clothit.server.api.dto.OutfitShortDto
import com.clothit.server.api.dto.OutfitShortListDto
import com.clothit.server.api.req.OutfitCreateReq
import com.clothit.server.api.req.OutfitUpdateReq
import com.clothit.server.service.OutfitService

class OutfitController(
    private val outfitService: OutfitService
) {

    fun save(req: OutfitCreateReq): IdDto{
        val id = outfitService.save(req)
        return IdDto(id)
    }

    fun get() : OutfitShortListDto {
        val outfitShortListDto = outfitService.getAll(1)
        return outfitShortListDto
    }

    fun get(outfitId: Int) : OutfitShortDto {
        val outfitShortDto = outfitService.getOneById(1, outfitId)
        return outfitShortDto
    }

    fun update(outfitId: Int, updateReq: OutfitUpdateReq){
        outfitService.update(outfitId, updateReq)
    }

    fun find(name : String) : OutfitShortListDto {
        return outfitService.find(name, 1)
    }

}

