package com.clothit.server.service.impl

import com.clothit.env.UserCreator
import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.dao.impl.FileDaoImpl
import com.clothit.server.dao.impl.ItemDaoImpl
import com.clothit.server.model.enums.ItemCategory
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.junit.Test
import java.io.File
import java.nio.file.Files
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

class ItemServiceTest {

    private val fileDao = FileDaoImpl
    private val itemDao = ItemDaoImpl
    private val itemService = ItemServiceImpl(itemDao, fileDao)
    private val fileService = FileServiceImpl(fileDao)

    @BeforeTest
    fun setup() {
        val PASSWORD_DATABASE = System.getenv("CLOTHITPASSWORD")
        requireNotNull(PASSWORD_DATABASE) { "Environment variable CLOTHITPASSWORD is not set" }

        Database.connect("jdbc:postgresql://localhost:5432/clothit_new", driver = "org.postgresql.Driver",
            user = "postgres", password = PASSWORD_DATABASE
        )
    }


    @Test
    fun saveItemPositive() {

        val user = UserCreator.createUserTest()

        val resource = javaClass.classLoader.getResource("files/itemTest.jpg")
        val file = File(resource!!.toURI())
        val multipart = fileToMultiPart(file)

        val fileId = runBlocking { fileService.save(multipart) }

        val req = ItemCreateReq(
            category = ItemCategory.BOTTOMS, description = "description", fileId = fileId
        )
        val itemId = itemService.save(req)

        assertNotNull(itemId)

    //    val item = itemService.getAll(user.userId)
   //       assertNotNull(item)
    }

    fun fileToMultiPart(file: File, partName: String = "file"): MultiPartData {
        val fileBytes = Files.readAllBytes(file.toPath())
        val fileItem = PartData.FileItem(
            provider = { file.inputStream().asInput() },
            dispose = {},
            partHeaders = headersOf(
                HttpHeaders.ContentDisposition to listOf(
                    ContentDisposition.File.withParameter(ContentDisposition.Parameters.Name, partName)
                        .withParameter(ContentDisposition.Parameters.FileName, file.name).toString()
                )
            )
        )
        return object : MultiPartData {
            private var read = false
            override suspend fun readPart() = if (read) null else {
                read = true
                fileItem
            }
        }
    }
}
