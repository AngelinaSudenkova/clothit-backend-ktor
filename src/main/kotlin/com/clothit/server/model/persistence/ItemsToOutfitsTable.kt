package com.clothit.server.model.persistence

import org.jetbrains.exposed.sql.Table

object ItemsToOutfitsTable : Table("items_to_outfits") {
    val id = integer("id").autoIncrement()
    val itemId = integer("item_id").references(ItemTable.id)
    val outfitId = integer("outfit_id").references(OutfitTable.id)

    override val primaryKey = PrimaryKey(id)
}