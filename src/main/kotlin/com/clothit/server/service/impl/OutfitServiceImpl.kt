package com.clothit.server.service.impl

import com.clothit.server.api.req.OutfitCreateReq
import com.clothit.server.dao.ItemsToOutfitsDao
import com.clothit.server.dao.OutfitDao
import com.clothit.server.model.entity.OutfitEntity
import com.clothit.server.service.OutfitService

class OutfitServiceImpl(
    private val outfitDao: OutfitDao,
    private val itemsToOutfitsDao: ItemsToOutfitsDao
): OutfitService {
    override fun save(req: OutfitCreateReq): Int {
        val outfitEntity = OutfitEntity(season = req.season,
            description = req.description,
            name = req.name)
        val outfitEntityId = outfitDao.save(outfitEntity)
        val itemsId = req.itemsId
        for(itemId in itemsId){
            itemsToOutfitsDao.save(outfitEntityId, itemId)
        }
        return outfitEntityId
    }
}