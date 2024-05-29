package com.clothit.server.service.impl

import com.clothit.error.ErrorCustomMessage
import com.clothit.error.ErrorTypes
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

        val itemEntity = ItemEntity(category = req.category, description = req.description)
        val fileEntity =
            fileDao.getById(req.fileId) ?: throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        val itemEntityId =
            itemDao.save(itemEntity) ?: throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()

        itemEntity.id = itemEntityId
        fileEntity.item = itemEntity
        fileDao.update(fileEntity)
        return itemEntityId
    }

    override fun getAll(userId: Long): ItemShortListDto {
        val listItemEntity = itemDao.getAll()
        val listShortItemDto = ArrayList<ItemShortDto>()
        if (listItemEntity.isEmpty()) {
            throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }

        for (item in listItemEntity) {
            val fileEntity = fileDao.getByItemId(item.id!!)
                ?: throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()

            val itemShortDto = item.toItemShortDto(fileId = fileEntity.id!!)
            listShortItemDto.add(itemShortDto)
        }
        return ItemShortListDto(listShortItemDto)
    }

    override fun updateItem(itemId: Int, req: ItemUpdateReq) {

            val itemEntity = itemDao.getById(itemId) ?: throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
            itemEntity.update(req)
            itemDao.update(itemEntity)
    }

    override fun getByCategory(categoryName: String, userId: Long): ItemShortListDto {
            val itemList = itemDao.getByCategory(categoryName)
            val listItemDto = ArrayList<ItemShortDto>()
            if (itemList.isEmpty()){throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()}
                for (item in itemList) {
                    val fileEntity = fileDao.getByItemId(item.id!!) ?:  throw ErrorCustomMessage(ErrorTypes.NOT_FOUND_EXCEPTION).toException()
                    val shortItemDto = item.toItemShortDto(fileId = fileEntity.id!!)
                    listItemDto.add(shortItemDto)
                }

            return ItemShortListDto(listItemDto)
        }
    }


