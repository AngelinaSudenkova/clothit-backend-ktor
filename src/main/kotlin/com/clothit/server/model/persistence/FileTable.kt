package com.clothit.server.model.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object FileTable: Table("files") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val size = long("size")
    val item_id = integer("item_id").references(ItemTable.id).index().nullable()
    val timeCreated = timestamp("time_created")
    val timeUpdated = timestamp("time_updated")

    override val primaryKey =  PrimaryKey(id)
}
