package com.clothit.server.dao

import com.clothit.server.model.entity.FileEntity

interface FileDao {
    fun save(entity: FileEntity)
    fun getById(id: Int): FileEntity?
    fun findById(id: Int): FileEntity
    fun getAllByItemId(itemId:Int): List<FileEntity>
    fun getByItemId(itemId: Int): FileEntity?
    fun getByOutfitId(outfitId: Int): List<FileEntity>?
    fun findByOutfitId(outfitId: Int): List<FileEntity>
    fun update(entity: FileEntity)
    fun deleteAllByOutfitId(outfitId: Int)
    fun findByItemId(id: Int): FileEntity
}