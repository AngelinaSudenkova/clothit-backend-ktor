package com.clothit.server.service.impl

import com.clothit.error.ExceptionCustomMessage
import com.clothit.error.ExceptionTypes
import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.UserDao
import com.clothit.server.model.entity.UserEntity
import com.clothit.server.service.TokenService
import com.clothit.server.service.UserService
import com.clothit.util.PasswordUtil
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserServiceImpl(
    private val userDao: UserDao,
    private val tokenService: TokenService,
) : UserService {


    override fun registerUser(req: UserRegisterReq): String {
        return transaction {
            userDao.unCheckIfExists(req.email)
            val newUser = UserEntity.of(req)
            userDao.save(newUser)
            val token = tokenService.createRefreshToken(newUser)
            token
        }
    }

    override fun authenticateUser(email: String, password: String): String {
        return transaction {
            val userEntity = userDao.findByEmail(email)
            if (!PasswordUtil.checkPassword(password, userEntity.passwordHash)) {
                throw ExceptionCustomMessage(ExceptionTypes.BAD_REQUEST).toException()
            }
            val token = tokenService.createRefreshToken(userEntity)
            token
        }
    }

    override fun getUser(userId: UUID): UserEntity {
        val userEntity = userDao.findById(userId)
        return userEntity
    }

    override fun deleteUser(userId: UUID) {
        userDao.delete(userId)
    }

    override fun searchByUsername(name: String?): List<UserDto> {
        val userEntityList = userDao.searchByUsername(name)
        return userEntityList.map { e -> e.toDto() }
    }

    override fun getByEmail(email: String): UserEntity {
        val userEntity = userDao.findByEmail(email)
        return userEntity
    }
}
