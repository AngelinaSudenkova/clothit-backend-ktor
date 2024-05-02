package com.clothit.server.service.impl

import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.dao.FileDao
import com.clothit.server.dao.ItemDao

import com.clothit.server.model.entity.ItemEntity
import com.clothit.server.service.ItemService

class ItemServiceImpl
(   private val itemDao: ItemDao,
    private val fileDao: FileDao
) : ItemService {


    override fun save(req: ItemCreateReq): Int {
        val itemEntity = ItemEntity(category = req.category, description = req.description)
        val fileEntity = fileDao.getById(req.fileId)
        val itemEntityId = itemDao.save(itemEntity)
        itemEntity.id = itemEntityId
        fileEntity.item = itemEntity
        fileDao.update(fileEntity)
        return itemEntityId

    }
}