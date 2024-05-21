package com.clothit.server.service.impl

import com.clothit.error.CustomException
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
        try {
            val itemEntity = ItemEntity(category = req.category, description = req.description)
            val fileEntity = fileDao.getById(req.fileId)
            val itemEntityId = itemDao.save(itemEntity)
            itemEntity.id = itemEntityId
            fileEntity.item = itemEntity
            fileDao.update(fileEntity)
            return itemEntityId
        } catch (e: Exception) {
            throw CustomException.ItemCanNotBeSavedException()

        }

    }

    override fun getAll(userId: Long): ItemShortListDto? {
        val listItemEntity = itemDao.getAll()
        val listShortItemDto = ArrayList<ItemShortDto>()

        for (item in listItemEntity) {
            val fileEntity = fileDao.getByItemId(item.id!!)
            val itemShortDto = item.toItemShortDto(fileId = fileEntity.id!!)
            listShortItemDto.add(itemShortDto)
        }
        return ItemShortListDto(listShortItemDto)
    }

    override fun updateItem(itemId: Int, req: ItemUpdateReq) {
        try {
            val itemEntity = itemDao.getById(itemId)
            itemEntity.update(req)
            itemDao.update(itemEntity)
        } catch (e: Exception) {
            throw CustomException.UpdateException()
        }
    }

    override fun getByCategory(categoryName: String, userId: Long): ItemShortListDto? {
        try {
            val itemList = itemDao.getByCategory(categoryName)
            val listItemDto = ArrayList<ItemShortDto>()
            if (itemList != null) {
                for (item in itemList) {
                    val fileEntity = fileDao.getByItemId(item.id!!)
                    val shortItemDto = item.toItemShortDto(fileId = fileEntity.id!!)
                    listItemDto.add(shortItemDto)
                }
            }
            return ItemShortListDto(listItemDto)
        } catch (e: Exception) {
            throw CustomException.ItemNotFoundException()
        }
    }


}