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
            val outfitEntity = OutfitEntity.outfitCreate(req)
            val outfitEntityId = outfitDao.save(outfitEntity)
            outfitEntity.id = outfitEntityId
            val itemsId = req.itemsId
            for (itemId in itemsId) {
                checkIfItemExists(itemId)
                itemsToOutfitsDao.save(outfitEntityId!!, itemId)
                val fileEntityItem = fileDao.findById(itemId)
                val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, outfitEntity)
                fileDao.save(fileEntityOutfit)
            }
            return outfitEntityId!!
    }


    override fun getAll(userId: Long): OutfitShortListDto {
        val listOutfitEntity = outfitDao.getAll()
        val listShortOutfitDto = ArrayList<OutfitShortDto>()
        for (outfit in listOutfitEntity) {
            val fileEntities = fileDao.findByOutfitId(outfit.id!!)
            listShortOutfitDto.add(outfit.toShortOutfitDto(fileEntities))
        }
        return OutfitShortListDto(listShortOutfitDto)
    }

    override fun getOneById(userId: Long, outfitId: Int): OutfitShortDto {
        val outfitEntity = outfitDao.findById(outfitId)
        val fileEntities = fileDao.findByOutfitId(outfitId)

        return outfitEntity.toShortOutfitDto(fileEntities)
    }


    override fun update(outfitId: Int, req: OutfitUpdateReq) {
        transaction {
            val oldOutfitEntity = outfitDao.findById(outfitId)
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
                val fileEntityItem = fileDao.findById(item)
                val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, oldOutfitEntity)
                fileDao.save(fileEntityOutfit)
            }
        }
    }


    override fun find(name: String, userId: Long): OutfitShortListDto {

        val listOutfitEntity = outfitDao.searchByWord(name)
        val listShortOutfitDto = ArrayList<OutfitShortDto>()

        for (outfit in listOutfitEntity) {
            val file = outfit.id?.let { fileDao.findByOutfitId(it) }
            listShortOutfitDto.add(outfit.toShortOutfitDto(file!!))
        }
        return OutfitShortListDto(listShortOutfitDto)
    }


    private fun checkIfItemExists(itemId: Int): Boolean {
        val ifExist = itemDao.checkIfExistsById(itemId)
        if (!ifExist) throw ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
        return ifExist
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