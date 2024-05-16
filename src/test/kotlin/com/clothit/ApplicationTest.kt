package com.clothit

import com.clothit.server.api.fileRoutingConfigure
import com.clothit.server.api.itemRoutingConfigure
import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.model.enums.ItemCategory
import com.clothit.server.model.persistence.FileTable
import com.clothit.server.model.persistence.ItemTable
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @BeforeTest
    fun setUpDB(){
        connectToInMemoryDatabase()
        setupDatabase()
    }

    @Test
    fun testItem() = testApplication {
        application {
            install(ContentNegotiation) {
                json()
            }
            itemRoutingConfigure()
            fileRoutingConfigure()
        }



        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val boundary = "TestBoundary"

        client.post("service/clothit/api/v1/file") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("description", "Test image")
                        append("image", File("D:\\Studia\\SEM6\\Nikita\\clothit-backend-uploadingImageFunc\\clothit-backend-uploadingImageFunc\\src\\test\\kotlin\\com\\clothit\\resources\\test.png").readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"test.png\"")
                        })
                    },
                    boundary,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary)
                )
            )
        }

        val itemCreateReq = ItemCreateReq(ItemCategory.BOTTOMS, "Test", 1)

        client.post("service/clothit/api/v1/item") {
            contentType(ContentType.Application.Json)
            setBody(itemCreateReq)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("id:1", bodyAsText())
        }
    }


    fun connectToInMemoryDatabase() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;"
        Database.connect(jdbcURL, driverClassName)
    }

    fun setupDatabase() {
        transaction {
            SchemaUtils.create(ItemTable)
            SchemaUtils.create(FileTable)
        }
    }
}
