package com.clothit.server.dao

import com.clothit.server.model.entity.OutfitEntity

interface OutfitDao {

    fun save(entity: OutfitEntity): Int?

    fun getById(id: Int): OutfitEntity?

    fun getAll(): List<OutfitEntity>

    fun update(entity: OutfitEntity)

    fun searchByWord(word: String): List<OutfitEntity>
}