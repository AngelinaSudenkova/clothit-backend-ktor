package com.clothit.server.dao

import com.clothit.server.model.entity.UserEntity
import java.util.*

interface UserDao {
    fun save(entity: UserEntity) : UUID
    fun getById(id: UUID) : UserEntity?
    fun update(entity: UserEntity) : String
    fun delete(id: UUID) : Boolean
    fun searchByUsername(name: String): List<UserEntity>
    fun searchByEmail(email: String) : UserEntity?
    fun checkIfExists(email: String): Boolean

}