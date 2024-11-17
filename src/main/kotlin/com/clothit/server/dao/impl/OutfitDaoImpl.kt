package com.clothit.server.dao.impl

import com.clothit.error.ExceptionCustomMessage
import com.clothit.error.ExceptionTypes
import com.clothit.server.dao.OutfitDao
import com.clothit.server.model.entity.OutfitEntity
import com.clothit.server.model.enums.OutfitSeason
import com.clothit.server.model.persistence.OutfitTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object OutfitDaoImpl: OutfitDao {
    override fun save(entity: OutfitEntity): Int? {
        var id :Int? = null
        transaction {
            val insertResult = OutfitTable.insert {
                it[season] = entity.season.name
                it[description] = entity.description
                it[name] = entity.name
                it[timeCreated] = entity.timeCreated
                it[timeUpdated] = entity.timeUpdated
            }
            id = insertResult[OutfitTable.id]
        }
        return id
    }

    override fun getById(id: Int): OutfitEntity? {
        val result = transaction { OutfitTable.selectAll().where { OutfitTable.id eq id }.singleOrNull() }
        return result?.let {
            OutfitEntity(
                it[OutfitTable.id],
                it[OutfitTable.season].let { t -> OutfitSeason.valueOf(t) },
                it[OutfitTable.description],
                it[OutfitTable.name],
                it[OutfitTable.timeCreated],
                it[OutfitTable.timeUpdated]
            )
        }
    }

    override fun findById(id: Int): OutfitEntity {
        val outfitEntity = getById(id)
        return outfitEntity ?: throw  ExceptionCustomMessage(ExceptionTypes.NOT_FOUND_EXCEPTION).toException()
    }


    override fun getAll(): List<OutfitEntity> {
        return transaction {
            OutfitTable.selectAll().map {
                OutfitEntity(
                    it[OutfitTable.id],
                    it[OutfitTable.season].let { t -> OutfitSeason.valueOf(t) },
                    it[OutfitTable.description],
                    it[OutfitTable.name],
                    it[OutfitTable.timeCreated],
                    it[OutfitTable.timeUpdated]
                )
            }
        }
    }

    override fun update(entity: OutfitEntity) {
        val result = transaction {
            OutfitTable.update({ OutfitTable.id eq entity.id!! }) {
                it[season] = entity.season.name
                it[description] = entity.description
                it[name] = entity.name
                it[timeCreated] = entity.timeCreated
                it[timeUpdated] = entity.timeUpdated
            }
        }
    }

    override fun searchByWord(word: String): List<OutfitEntity> {
            return transaction {
                OutfitTable.selectAll().where{OutfitTable.name like "%$word%"}.map {
                    OutfitEntity(
                        it[OutfitTable.id],
                        it[OutfitTable.season].let { t -> OutfitSeason.valueOf(t) },
                        it[OutfitTable.description],
                        it[OutfitTable.name],
                        it[OutfitTable.timeCreated],
                        it[OutfitTable.timeUpdated]
                    )
                }
            }
        }
    }
