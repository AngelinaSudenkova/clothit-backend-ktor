package com.clothit.server.service.impl

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
): OutfitService {
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
                if (!checkIfItemExists(itemId)) throw NoSuchElementException("Item with id $itemId not found")
                itemsToOutfitsDao.save(outfitEntityId, itemId)
                val fileEntityItem = fileDao.getById(itemId)
                val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, outfitEntity)
                fileDao.save(fileEntityOutfit)
            }
            outfitEntityId
        }
    }

    override fun getAll(userId: Long): OutfitShortListDto {
        val listOutfitEntity = outfitDao.getAll()
        val listShortOutfitDto = ArrayList<OutfitShortDto>()

        for(outfit in listOutfitEntity){
            val fileEntities = fileDao.getByOutfitId(outfit.id!!)
            listShortOutfitDto.add(outfit.toShortOutfitDto(fileEntities))
        }
        return OutfitShortListDto(listShortOutfitDto)
    }

    override fun getOneById(userId: Long, outfitId: Int): OutfitShortDto {
        val outfitEntity = outfitDao.getById(outfitId)
        val fileEntities = fileDao.getByOutfitId(outfitId)
        return outfitEntity.toShortOutfitDto(fileEntities)
    }

    override fun update(outfitId: Int, req: OutfitUpdateReq) {
        val oldOutfitEntity = outfitDao.getById(outfitId)
        oldOutfitEntity.update(req)
        outfitDao.update(oldOutfitEntity)
        val itemsId = req.itemsId
        val analysisObj = analysisItems(outfitId, itemsId)
        analysisObj.tie.forEach { itemId ->
            itemsToOutfitsDao.save(outfitId, itemId)
        }
        analysisObj.delete.forEach { itemsToOutfitsDao.delete(outfitId,it)
        }
        fileDao.delete(outfitId)
        val newItems = itemsToOutfitsDao.getItemsIdByOutfitId(outfitId)
        for(item in newItems) {
            val fileEntityItem = fileDao.getById(item)
            val fileEntityOutfit = createOutfitFileEntity(fileEntityItem, oldOutfitEntity)
            fileDao.save(fileEntityOutfit)
        }

    }


    private fun checkIfItemExists(itemId: Int) : Boolean{
        return itemDao.checkIfExistsById(itemId)
    }

    private fun createOutfitFileEntity(fileEntityItem: FileEntity, outfitEntity: OutfitEntity) : FileEntity{
        val fileEntityOutfit = fileEntityItem.copy()
        fileEntityOutfit.item = null
        fileEntityOutfit.outfit = outfitEntity
        return fileEntityOutfit
    }


    inner class OutfitItemsAnalysisObj(
        val tie: List<Int>,
        val delete: List<Int>
    )

    fun analysisItems(outfitId: Int,  itemsId : List<Int>) : OutfitItemsAnalysisObj{
        var deletedIds: MutableList<Int> = itemsToOutfitsDao.getItemsIdByOutfitId(outfitId).toMutableList()
        val addingItems : MutableList<Int> = mutableListOf()
        for(item in itemsId){
            if(!deletedIds.contains(item)){
                addingItems.add(item)
            }
        }

        val itemsIds: List<Int> = itemsId
        val iterator = deletedIds.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (itemsIds.contains(item)) {
                iterator.remove()
            }

        }

        return OutfitItemsAnalysisObj(addingItems, deletedIds)
    }

}