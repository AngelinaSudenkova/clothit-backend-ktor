package com.clothit.server.dao.impl

import com.clothit.server.dao.FileDao
import com.clothit.server.model.entity.FileEntity
import com.clothit.server.model.entity.ItemEntity
import com.clothit.server.model.enums.ItemCategory
import com.clothit.server.model.persistence.FileTable
import com.clothit.server.model.persistence.ItemTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


object FileDaoImpl : FileDao {

    override fun save(entity: FileEntity): Int {
        var id = -1
        transaction {
            val insertResult = FileTable.insert {
                it[name] = entity.name
                it[size] = entity.size
                it[item_id] = entity.item?.id
                it[timeCreated] = entity.timeCreated
                it[timeUpdated] = entity.timeUpdated
            }
            id = insertResult[FileTable.id]
        }
        return id
    }

    override fun getById(id: Int): FileEntity {
        var result: ResultRow? = null
        transaction {
            result = (FileTable leftJoin ItemTable).selectAll()
                .where { FileTable.id eq id }
                .singleOrNull()
        }

        return result?.let {
            FileEntity(
                it[FileTable.id],
                it[FileTable.name],
                it[FileTable.size],
    //                ItemEntity(
    //                    it[ItemTable.id],
    //                    it[ItemTable.category].let { t -> ItemCategory.valueOf(t) },
    //                    it[ItemTable.description],
    //                    it[ItemTable.timeCreated]
    //                ),
                null,
                it[FileTable.timeCreated],
                it[FileTable.timeUpdated]
            )
        } ?: throw NoSuchElementException("File with id $id not found")
    }

    override fun getAllByItemId(itemId: Int): List<FileEntity> {
        val result = (FileTable innerJoin ItemTable).select(FileTable.item_id eq ItemTable.id)
            .where { FileTable.item_id eq itemId }
        return transaction {
            result.map {
                FileEntity(
                    it[FileTable.id],
                    it[FileTable.name],
                    it[FileTable.size],
                    ItemEntity(
                        it[ItemTable.id],
                        it[ItemTable.category].let { t -> ItemCategory.valueOf(t) },
                        it[ItemTable.description],
                        it[ItemTable.timeCreated]
                    ),
                    it[FileTable.timeCreated],
                    it[FileTable.timeUpdated]
                )
            }
        }
    }

    override fun update(entity: FileEntity){

        transaction {
            val updateCount = FileTable.update({ FileTable.id eq entity.id!! }) {
                it[name] = entity.name
                it[size] = entity.size
                it[item_id] = entity.item?.id
                it[timeCreated] = entity.timeCreated
                it[timeUpdated] = entity.timeUpdated
            }
        }
    }
}