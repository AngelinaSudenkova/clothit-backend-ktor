package com.clothit.server.model.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OutfitTable : Table("outfits") {
    val id = integer("id").autoIncrement()
    val season = varchar("season", 25)
    val description = varchar("description", 255)
    val name = varchar("name", 50)
    val timeCreated = timestamp("time_created")
    val timeUpdated = timestamp("time_updated")

    override val primaryKey = PrimaryKey(id)
}