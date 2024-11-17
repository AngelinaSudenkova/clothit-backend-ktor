package com.clothit.server.service.impl

import com.clothit.error.ExceptionCustomMessage
import com.clothit.error.ExceptionTypes
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
                if (!checkIfItemExists(itemId)) throw (ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException())
                itemsToOutfitsDao.save(outfitEntityId!!, itemId)
                val fileEntityItem = fileDao.getById(itemId)
                    ?: throw (ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException())
                val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, outfitEntity)
                fileDao.save(fileEntityOutfit)
            }
            outfitEntityId!!
        }
    }


    override fun getAll(userId: Long): OutfitShortListDto {
        val listOutfitEntity = outfitDao.getAll()
        val listShortOutfitDto = ArrayList<OutfitShortDto>()
        if (listOutfitEntity.isEmpty()) {
            throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
        }
        for (outfit in listOutfitEntity) {
            val fileEntities = fileDao.getByOutfitId(outfit.id!!)
                ?: throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
            listShortOutfitDto.add(outfit.toShortOutfitDto(fileEntities))
        }
        return OutfitShortListDto(listShortOutfitDto)
    }

    override fun getOneById(userId: Long, outfitId: Int): OutfitShortDto {

        val outfitEntity = outfitDao.getById(outfitId)
            ?: throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()

        val fileEntities = fileDao.getByOutfitId(outfitId)
            ?: throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()

        return outfitEntity.toShortOutfitDto(fileEntities)
    }


    override fun update(outfitId: Int, req: OutfitUpdateReq) {
        transaction {
            val oldOutfitEntity = outfitDao.getById(outfitId)
                ?: throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()

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
                    ?: throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
                val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, oldOutfitEntity)
                fileDao.save(fileEntityOutfit)
            }
        }
    }


    override fun find(name: String, userId: Long): OutfitShortListDto {

        val listOutfitEntity = outfitDao.searchByWord(name)
        if (listOutfitEntity.isEmpty()) {
            throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
        }

        val listShortOutfitDto = ArrayList<OutfitShortDto>()
        for (outfit in listOutfitEntity) {
            val file = outfit.id?.let { fileDao.getByOutfitId(it) }
                ?: throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
            listShortOutfitDto.add(outfit.toShortOutfitDto(file))
        }
        return OutfitShortListDto(listShortOutfitDto)
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

    private fun analysisItems(outfitId: Int, itemsId: List<Int>): OutfitItemsAnalysisObj {
        val deletedIds: MutableSet<Int> = itemsToOutfitsDao
            .getItemsIdByOutfitId(outfitId).toMutableSet()

        val addingItems: MutableSet<Int> = itemsId.toHashSet()

        val commonIds = deletedIds.intersect(itemsId.toSet())
        deletedIds.removeAll(commonIds)
        addingItems.removeAll(commonIds)

        return OutfitItemsAnalysisObj(addingItems.toList(), deletedIds.toList())
    }

}