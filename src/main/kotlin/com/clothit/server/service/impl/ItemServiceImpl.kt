package com.clothit.server.service.impl

import com.clothit.error.ExceptionCustomMessage
import com.clothit.error.ExceptionTypes
import com.clothit.server.api.dto.ItemShortDto
import com.clothit.server.api.dto.ItemShortListDto
import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.api.req.ItemUpdateReq
import com.clothit.server.dao.FileDao
import com.clothit.server.dao.ItemDao
import com.clothit.server.model.entity.ItemEntity
import com.clothit.server.service.ItemService

class ItemServiceImpl
    (
    private val itemDao: ItemDao,
    private val fileDao: FileDao
) : ItemService {


    override fun save(req: ItemCreateReq): Int {
        val itemEntity = ItemEntity.itemCreate(req)
        itemDao.save(itemEntity)
        if (req.fileId != null) {
            val fileEntity = fileDao.findById(req.fileId)
            fileEntity.item = itemEntity
            fileDao.update(fileEntity)
        }
        return itemEntity.id!!
    }

    override fun getAll(userId: Long): ItemShortListDto {
        val listItemEntity = itemDao.getAll()
        val listShortItemDto = ArrayList<ItemShortDto>()
        for (item in listItemEntity) {
            val fileEntity = fileDao.findById(item.id!!)
            val itemShortDto = item.toItemShortDto(fileId = fileEntity.id!!)
            listShortItemDto.add(itemShortDto)
        }
        return ItemShortListDto(listShortItemDto)
    }

    override fun updateItem(itemId: Int, req: ItemUpdateReq) {
        val itemEntity = itemDao.findById(itemId)
        itemEntity.update(req)
        itemDao.update(itemEntity)
    }

    override fun getByCategory(categoryName: String, userId: Long): ItemShortListDto {
        val itemList = itemDao.getByCategory(categoryName)
        val listItemDto = ArrayList<ItemShortDto>()
        itemList.forEach {item ->
            run {
                val fileEntity = fileDao.findByItemId(item.id!!)
                val shortItemDto = item.toItemShortDto(fileId = fileEntity.id!!)
                listItemDto.add(shortItemDto)
            }
        }
        return ItemShortListDto(listItemDto)
    }
}


