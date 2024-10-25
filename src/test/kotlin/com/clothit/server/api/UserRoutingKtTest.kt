package com.clothit.server.api

import com.clothit.config.serializationConfigure
import com.clothit.module
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class UserRoutingKtTest {

    @Test
    fun testLoginUser() = testApplication {
        application {
            module()
            serializationConfigure()
        }

        client.post("/service/clothit/api/v1/login").apply {

        }
    }
}