package com.clothit.server.dao

import com.clothit.server.model.entity.ItemEntity

interface ItemDao {

    fun save(entity: ItemEntity):Int;

    fun getById(id: Int): ItemEntity;

    fun getAll(): List<ItemEntity>;
}