package com.clothit.server.service

import com.clothit.server.api.dto.ItemShortListDto
import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.api.req.ItemUpdateReq

interface ItemService {

    fun save(req: ItemCreateReq): Int

    fun getAll(userId: Long) : ItemShortListDto?

    fun updateItem(itemId : Int, req : ItemUpdateReq)

    fun getByCategory(categoryName: String, userId: Long) : ItemShortListDto?
}