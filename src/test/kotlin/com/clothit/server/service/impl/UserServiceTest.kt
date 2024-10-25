package com.clothit.server.service.impl

import com.clothit.server.api.dto.UserDto
import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.UserDao
import com.clothit.server.model.entity.UserEntity
import com.clothit.util.DateTimeUtil
import com.clothit.util.PasswordUtil
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserServiceTest {



    private val userDaoMock = mockk<UserDao>()
    private val jwtService = mockk<JwtServiceImpl>()
    private val userService = UserServiceImpl(userDaoMock, jwtService)

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testRegisterUser() {
        val userRegisterReq = UserRegisterReq("test@example.com", "password", "testuser")
        val hashedPassword = PasswordUtil.hashPassword(userRegisterReq.password)
        val userEntity = UserEntity(
            id = UUID.randomUUID(),
            email = "test@example.com",
            passwordHash = hashedPassword,
            username = "testuser",
            registeredDate = DateTimeUtil.getCurrentTime(),
            lastLoginDate = DateTimeUtil.getCurrentTime(),
            isActive = true
        )

        every { userDaoMock.checkIfExists(userRegisterReq.email) } returns false
        every { userDaoMock.save(any()) } returns userEntity.id!!

        val result = userService.registerUser(userRegisterReq)

        assertNotNull(result)

        verify { userDaoMock.checkIfExists(userRegisterReq.email) }
        verify { userDaoMock.save(any()) }
    }

    @Test
    fun testAuthenticateUser() {
        val email = "test@example.com"
        val password = "password"
        val hashedPassword = PasswordUtil.hashPassword(password)
        val userEntity = UserEntity(
            id = UUID.randomUUID(),
            email = email,
            passwordHash = hashedPassword,
            username = "testuser",
            registeredDate = DateTimeUtil.getCurrentTime(),
            lastLoginDate = DateTimeUtil.getCurrentTime(),
            isActive = true
        )
        val userDto = UserDto(userEntity.id!!, userEntity.username)

        every { userDaoMock.searchByEmail(email) } returns userEntity

        val result = userService.authenticateUser(email, password)

        assertNotNull(result)

        verify { userDaoMock.searchByEmail(email) }
    }

    @Test
    fun testGetUser() {
        val userId = UUID.randomUUID()
        val userEntity = UserEntity(
            id = userId,
            email = "test@example.com",
            passwordHash = "hashedPassword",
            username = "testuser",
            registeredDate = DateTimeUtil.getCurrentTime(),
            lastLoginDate = DateTimeUtil.getCurrentTime(),
            isActive = true
        )

        every { userDaoMock.getById(userId) } returns userEntity

        val result = userService.getUser(userId)

        assertNotNull(result)

        verify { userDaoMock.getById(userId) }
    }

    @Test
    fun testGetByEmail() {
        val email = "test@example.com"
        val userEntity = UserEntity(
            id = UUID.randomUUID(),
            email = email,
            passwordHash = "hashedPassword",
            username = "testuser",
            registeredDate = DateTimeUtil.getCurrentTime(),
            lastLoginDate = DateTimeUtil.getCurrentTime(),
            isActive = true
        )

        every { userDaoMock.searchByEmail(email) } returns userEntity

        val result = userService.getByEmail(email)

        assertNotNull(result)

        verify { userDaoMock.searchByEmail(email) }
    }

    @Test
    fun testDeleteUser() {
        val userId = UUID.randomUUID()

        every { userDaoMock.delete(userId) } returns true

        userService.deleteUser(userId)

        verify { userDaoMock.delete(userId) }
    }

    @Test
    fun testSearchByUsername() {
        val username = "testuser"
        val userEntity = UserEntity(
            id = UUID.randomUUID(),
            email = "test@example.com",
            passwordHash = "hashedPassword",
            username = username,
            registeredDate = DateTimeUtil.getCurrentTime(),
            lastLoginDate = DateTimeUtil.getCurrentTime(),
            isActive = true
        )
        val userDto = UserDto(userEntity.id!!, userEntity.username)
        val userList = listOf(userEntity)
        val userDtoList = listOf(userDto)

        every { userDaoMock.searchByUsername(username) } returns userList

        val result = userService.searchByUsername(username)

        assertNotNull(result)
        assertEquals(userDtoList, result)
        verify { userDaoMock.searchByUsername(username) }
    }

}