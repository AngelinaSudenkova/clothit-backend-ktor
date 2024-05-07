package com.clothit.server.service

import com.clothit.server.api.dto.OutfitShortListDto
import com.clothit.server.api.req.OutfitCreateReq

interface OutfitService {
    fun save(req: OutfitCreateReq): Int

    fun getAll(userId : Long) : OutfitShortListDto
}