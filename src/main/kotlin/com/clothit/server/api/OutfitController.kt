package com.clothit.server.api

import com.clothit.server.api.dto.IdDto
import com.clothit.server.api.req.OutfitCreateReq
import com.clothit.server.dao.impl.ItemDaoImpl
import com.clothit.server.dao.impl.ItemsToOutfitDaoImpl
import com.clothit.server.dao.impl.OutfitDaoImpl
import com.clothit.server.service.OutfitService
import com.clothit.server.service.impl.OutfitServiceImpl

class OutfitController(private val outfitService: OutfitService =
    OutfitServiceImpl(OutfitDaoImpl,ItemDaoImpl, ItemsToOutfitDaoImpl )) {

    fun save(req: OutfitCreateReq): IdDto{
        val id = outfitService.save(req)
        return IdDto(id)
    }

}

