package com.clothit.server.api

import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class ItemRoutingKtTest {

    @Test
    fun testPostServiceClothitApiV1Item() = testApplication {
        application {
          //  configureLogoutRouting()
        }
        client.post("/service/clothit/api/v1/item").apply {
            TODO("Please write your test here")
        }
    }
}