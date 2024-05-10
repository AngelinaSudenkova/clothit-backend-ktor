package com.clothit.server.dao.impl

import com.clothit.server.dao.ItemDao
import com.clothit.server.model.entity.ItemEntity
import com.clothit.server.model.enums.ItemCategory
import com.clothit.server.model.persistence.ItemTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object ItemDaoImpl : ItemDao {


    override fun save(entity: ItemEntity): Int {
        var id = -1
        transaction {
            val insertResult = ItemTable.insert {
                it[category] = entity.category!!.name
                it[description] = entity.description
                it[timeCreated] = entity.timeCreation
            }
            id = insertResult[ItemTable.id]
        }
        return id
    }

    override fun getById(id: Int): ItemEntity {

         val result = transaction {  ItemTable.selectAll().where { ItemTable.id eq id }.singleOrNull()}

        return transaction {  result?.let {
            ItemEntity(
                it[ItemTable.id],
                it[ItemTable.category].let { t -> ItemCategory.valueOf(t) },
                it[ItemTable.description],
                it[ItemTable.timeCreated]
            )
        }} ?: throw NoSuchElementException("Item with id $id not found") //TODO return custom exception
    }

    override fun getAll(): List<ItemEntity> {
        return transaction {
            ItemTable.selectAll().map {
                ItemEntity(
                    it[ItemTable.id],
                    it[ItemTable.category].let { t -> ItemCategory.valueOf(t) },
                    it[ItemTable.description],
                    it[ItemTable.timeCreated]
                )
            }
        }
    }

    override fun checkIfExistsById(id: Int): Boolean {
        var exists = false
        transaction {
            val result = ItemTable
                .select(ItemTable.id)
                .where { ItemTable.id eq id }
                .limit(1)
                .toList()
            exists = result.isNotEmpty()
        }
        return exists
    }

    override fun update(entity: ItemEntity) {

        transaction {
            val result = ItemTable.update({ ItemTable.id eq entity.id!! }){
                it[description] = entity.description
                it[category] = entity.category!!.name
            }
        }

    }

}