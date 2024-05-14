package com.clothit.server.api

import com.clothit.server.api.dto.IdDto
import com.clothit.server.api.dto.ItemShortListDto
import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.api.req.ItemUpdateReq
import com.clothit.server.dao.impl.FileDaoImpl
import com.clothit.server.dao.impl.ItemDaoImpl
import com.clothit.server.service.ItemService

import com.clothit.server.service.impl.ItemServiceImpl

class ItemController(private val itemService: ItemService = ItemServiceImpl(ItemDaoImpl, FileDaoImpl)) {

    fun save(req: ItemCreateReq): IdDto {
        val id = itemService.save(req);
        return IdDto(id)
    }

    fun get() : ItemShortListDto? {
        val itemShortListDto = itemService.getAll(1)
        //TODO(authorization session)
        return itemShortListDto
    }

    fun update(itemId : Int, req: ItemUpdateReq) {
        itemService.updateItem(itemId, req)
    }

    fun getByCategory(categoryName: String) : ItemShortListDto? {
        return itemService.getByCategory(categoryName, 1)

    }

}