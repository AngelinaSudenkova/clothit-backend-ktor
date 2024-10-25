package com.clothit.server.service.impl

import com.clothit.server.api.req.UserRegisterReq
import com.clothit.server.dao.impl.TokenDaoImpl
import com.clothit.server.dao.impl.UserDaoImpl
import org.jetbrains.exposed.sql.Database
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class AuthServiceTest {

    private val userDao = UserDaoImpl
    private val tokenDao = TokenDaoImpl
    private val tokenService = JwtServiceImpl(userDao, tokenDao)
    private val userService = UserServiceImpl(userDao, tokenService)


    @BeforeTest
    fun setup() {
        val PASSWORD_DATABASE = System.getenv("CLOTHITPASSWORD")
        requireNotNull(PASSWORD_DATABASE) { "Environment variable CLOTHITPASSWORD is not set" }

        Database.connect("jdbc:postgresql://localhost:5432/clothit_new", driver = "org.postgresql.Driver",
            user = "postgres", password = PASSWORD_DATABASE
        )
    }

    @Test
    fun testRegisterUserPositive() {
        val userReq = UserRegisterReq(
            email = "test@test.com",
            password = "password",
            username = "testuser"
        )

       val token =  userService.registerUser(userReq)
        assertNotNull(token)

        val tokenSignIn = userService.authenticateUser(userReq.email, userReq.password)
        assertNotNull(tokenSignIn)

    }

}