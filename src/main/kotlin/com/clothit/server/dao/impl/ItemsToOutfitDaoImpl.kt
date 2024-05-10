package com.clothit.server.dao.impl

import com.clothit.server.dao.ItemsToOutfitsDao
import com.clothit.server.model.persistence.ItemsToOutfitsTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object ItemsToOutfitDaoImpl : ItemsToOutfitsDao {
    override fun save(outfitId: Int, itemId: Int): Int {
        var id = -1
        transaction {
            val inserResult = ItemsToOutfitsTable.insert {
                it[ItemsToOutfitsTable.itemId] = itemId
                it[ItemsToOutfitsTable.outfitId] = outfitId
            }
            id = inserResult[ItemsToOutfitsTable.id]
        }
        return id
    }

    override fun getItemsIdByOutfitId(outfitId: Int): List<Int> {
        return transaction {
            ItemsToOutfitsTable
                .selectAll()
                .where { ItemsToOutfitsTable.outfitId eq outfitId }
                .map { it[ItemsToOutfitsTable.itemId] }
                .toList()
        }
    }

    override fun delete(outfitId: Int, itemId: Int) {
        transaction {
            ItemsToOutfitsTable.deleteWhere  {
                (ItemsToOutfitsTable.itemId eq itemId) and
                        (ItemsToOutfitsTable.outfitId eq outfitId)
            }
        }
    }
}