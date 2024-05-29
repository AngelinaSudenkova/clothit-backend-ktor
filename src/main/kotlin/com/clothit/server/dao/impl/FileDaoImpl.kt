package com.clothit.server.dao.impl

import com.clothit.server.dao.FileDao
import com.clothit.server.model.entity.FileEntity
import com.clothit.server.model.entity.ItemEntity
import com.clothit.server.model.entity.OutfitEntity
import com.clothit.server.model.enums.ItemCategory
import com.clothit.server.model.enums.OutfitSeason
import com.clothit.server.model.persistence.FileTable
import com.clothit.server.model.persistence.ItemTable
import com.clothit.server.model.persistence.OutfitTable
import com.clothit.util.DateTimeUtil
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


object FileDaoImpl : FileDao {

    override fun save(entity: FileEntity): Int? {
        var id: Int? = null
        transaction {
            val insertResult = FileTable.insert {
                it[name] = entity.name
                it[size] = entity.size
                it[item_id] = entity.item?.id
                it[outfitId] = entity.outfit?.id
                it[timeCreated] = entity.timeCreated
                it[timeUpdated] = entity.timeUpdated
            }
            id = insertResult[FileTable.id]
        }
        return id
    }

    override fun getById(id: Int): FileEntity? {
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
                    ItemEntity(
                        it[ItemTable.id],
                      //  it[ItemTable.category].let { t -> ItemCategory.valueOf(t) },
                        ItemCategory.BOTTOMS,
                        it[ItemTable.description],
                        it[ItemTable.timeCreated] ?: DateTimeUtil.getCurrentTime()
                    ),
                null,
                it[FileTable.timeCreated],
                it[FileTable.timeUpdated]
            )
        }
    }

    override fun getAllByItemId(itemId: Int): List<FileEntity> {
        val result = (FileTable innerJoin ItemTable).selectAll().where(FileTable.item_id eq ItemTable.id)
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
                    null,
                    it[FileTable.timeCreated],
                    it[FileTable.timeUpdated]
                )
            }
        }
    }

    override fun getByItemId(itemId: Int): FileEntity? {
        val result = transaction {
            (FileTable innerJoin ItemTable)
                .select { FileTable.item_id eq ItemTable.id and (FileTable.item_id eq itemId) }
                .singleOrNull()
        }
        return result?.let {
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
                null,
                it[FileTable.timeCreated],
                it[FileTable.timeUpdated]
            )
        }
    }

    override fun getByOutfitId(outfitId: Int): List<FileEntity> {
        return transaction {
            val result = (FileTable innerJoin OutfitTable).selectAll()
                .where { FileTable.outfitId eq OutfitTable.id }
                .andWhere { FileTable.outfitId eq outfitId }

            result.map {
                FileEntity(
                    it[FileTable.id],
                    it[FileTable.name],
                    it[FileTable.size],
                    null,
                    OutfitEntity(
                        it[OutfitTable.id],
                        it[OutfitTable.season].let { t -> OutfitSeason.valueOf(t) },
                        it[OutfitTable.description],
                        it[OutfitTable.name],
                        it[OutfitTable.timeCreated],
                        it[OutfitTable.timeUpdated]
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
                it[outfitId] = entity.outfit?.id
                it[timeCreated] = entity.timeCreated
                it[timeUpdated] = entity.timeUpdated
            }
        }
    }

    override fun deleteAllByOutfitId(outfitId: Int) {
        transaction {
            FileTable.deleteWhere {
                (FileTable.outfitId eq outfitId)
            }
        }
    }
}