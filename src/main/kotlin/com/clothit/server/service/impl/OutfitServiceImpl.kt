package com.clothit.server.service.impl

import com.clothit.error.ErrorCustomMessage
import com.clothit.error.ErrorTypes
import com.clothit.server.api.dto.OutfitShortDto
import com.clothit.server.api.dto.OutfitShortListDto
import com.clothit.server.api.req.OutfitCreateReq
import com.clothit.server.api.req.OutfitUpdateReq
import com.clothit.server.dao.FileDao
import com.clothit.server.dao.ItemDao
import com.clothit.server.dao.ItemsToOutfitsDao
import com.clothit.server.dao.OutfitDao
import com.clothit.server.model.entity.FileEntity
import com.clothit.server.model.entity.OutfitEntity
import com.clothit.server.service.OutfitService
import org.jetbrains.exposed.sql.transactions.transaction

class OutfitServiceImpl(
    private val outfitDao: OutfitDao,
    private val itemDao: ItemDao,
    private val itemsToOutfitsDao: ItemsToOutfitsDao,
    private val fileDao: FileDao
) : OutfitService {
    override fun save(req: OutfitCreateReq): Int {
        try {
            return transaction {
                val outfitEntity = OutfitEntity(
                    season = req.season,
                    description = req.description,
                    name = req.name
                )
                val outfitEntityId = outfitDao.save(outfitEntity)
                outfitEntity.id = outfitEntityId
                val itemsId = req.itemsId
                for (itemId in itemsId) {
                    if (!checkIfItemExists(itemId)) throw NoSuchElementException("Item with id $itemId not found")
                    itemsToOutfitsDao.save(outfitEntityId, itemId)
                    val fileEntityItem = fileDao.getById(itemId)
                    val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, outfitEntity)
                    fileDao.save(fileEntityOutfit)
                }
                outfitEntityId
            }
        } catch (e: Exception) {
            throw ErrorCustomMessage(e, ErrorTypes.INTERNAL_SERVER_ERROR).toException()
        }
    }

    override fun getAll(userId: Long): OutfitShortListDto {
        val listOutfitEntity = outfitDao.getAll()
        val listShortOutfitDto = ArrayList<OutfitShortDto>()

        for (outfit in listOutfitEntity) {
            val fileEntities = fileDao.getByOutfitId(outfit.id!!)
            listShortOutfitDto.add(outfit.toShortOutfitDto(fileEntities))
        }
        return OutfitShortListDto(listShortOutfitDto)
    }

    override fun getOneById(userId: Long, outfitId: Int): OutfitShortDto {
        return try {
            val outfitEntity = outfitDao.getById(outfitId)
            val fileEntities = fileDao.getByOutfitId(outfitId)
            outfitEntity.toShortOutfitDto(fileEntities)
        } catch (e: Exception) {
            throw  ErrorCustomMessage(e, ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }
    }


    override fun update(outfitId: Int, req: OutfitUpdateReq) {
        try {
            transaction {
                val oldOutfitEntity = outfitDao.getById(outfitId)
                oldOutfitEntity.update(req)
                outfitDao.update(oldOutfitEntity)
                val itemsId = req.itemsId
                val analysisObj = analysisItems(outfitId, itemsId)
                analysisObj.tie.forEach { itemId ->
                    itemsToOutfitsDao.save(outfitId, itemId)
                }
                analysisObj.delete.forEach {
                    itemsToOutfitsDao.delete(outfitId, it)
                }
                fileDao.deleteAllByOutfitId(outfitId)
                val newItems = itemsToOutfitsDao.getItemsIdByOutfitId(outfitId)
                for (item in newItems) {
                    val fileEntityItem = fileDao.getById(item)
                    val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, oldOutfitEntity)
                    fileDao.save(fileEntityOutfit)
                }
            }
        } catch (e: Exception) {
            throw  ErrorCustomMessage(e, ErrorTypes.INTERNAL_SERVER_ERROR).toException()
        }
    }

    override fun find(name: String, userId: Long): OutfitShortListDto {
        try {
            val listOutfitEntity = outfitDao.searchByWord(name)
            val listShortOutfitDto = ArrayList<OutfitShortDto>()
            for (outfit in listOutfitEntity) {
                val file = outfit.id?.let { fileDao.getByOutfitId(it) }
                if (file != null) {
                    listShortOutfitDto.add(outfit.toShortOutfitDto(file))
                }
            }
            return OutfitShortListDto(listShortOutfitDto)
        } catch (e: Exception) {
            throw ErrorCustomMessage(e, ErrorTypes.NOT_FOUND_EXCEPTION).toException()
        }
    }


    private fun checkIfItemExists(itemId: Int): Boolean {
        return itemDao.checkIfExistsById(itemId)
    }

    private fun createOutfitFileEntity(fileEntityItem: FileEntity, outfitEntity: OutfitEntity): FileEntity {
        val fileEntityOutfit = fileEntityItem.copy()
        fileEntityOutfit.item = null
        fileEntityOutfit.outfit = outfitEntity
        return fileEntityOutfit
    }


    inner class OutfitItemsAnalysisObj(
        val tie: List<Int>,
        val delete: List<Int>
    )

    fun analysisItems(outfitId: Int, itemsId: List<Int>): OutfitItemsAnalysisObj {
        var deletedIds: MutableSet<Int> = itemsToOutfitsDao
            .getItemsIdByOutfitId(outfitId).toMutableSet()

        val addingItems: MutableSet<Int> = itemsId.toHashSet()

        val commonIds = deletedIds.intersect(itemsId)
        deletedIds.removeAll(commonIds)
        addingItems.removeAll(commonIds)

        return OutfitItemsAnalysisObj(addingItems.toList(), deletedIds.toList())
    }

}