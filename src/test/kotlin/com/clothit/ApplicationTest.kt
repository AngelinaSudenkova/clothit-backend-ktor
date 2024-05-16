package com.clothit

import com.clothit.server.api.dto.ItemShortListDto
import com.clothit.server.api.req.ItemCreateReq
import com.clothit.server.api.req.ItemUpdateReq
import com.clothit.server.dao.FileDao
import com.clothit.server.dao.ItemDao
import com.clothit.server.model.entity.FileEntity
import com.clothit.server.model.entity.ItemEntity
import com.clothit.server.model.enums.ItemCategory
import com.clothit.server.service.ItemService
import com.clothit.server.service.impl.ItemServiceImpl
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import kotlin.test.Test


class ApplicationTest {

    private lateinit var itemService: ItemService
    private val itemDao: ItemDao = mockk()
    private val fileDao: FileDao = mockk()


    @Before
    fun setUp() {
        itemService = ItemServiceImpl(itemDao, fileDao)
    }

    @Test
    fun testSaveMethod() = testApplication {
        // Given
        val req = ItemCreateReq(ItemCategory.BOTTOMS, "Test Description", 1)
        val itemId = 1
        val mockFileEntity = mockk<FileEntity>()
        every { itemDao.save(any()) } returns itemId
        every { fileDao.getById(any()) } returns mockFileEntity
        every { fileDao.update(any()) } returns Unit

        // When
        val result = itemService.save(req)

        // Then
        assertEquals(itemId, result)
    }

    @Test
    fun testGetAllMethod() = testApplication  {
        // Given
        val userId = 1L
        val itemEntity = mockk<ItemEntity>()
        val mockFileEntity = mockk<FileEntity>()
        every { itemDao.getAll() } returns listOf(itemEntity)
        every { fileDao.getByItemId(any()) } returns mockFileEntity
        every { itemEntity.toItemShortDto(any()) } returns mockk()

        // When
        val result: ItemShortListDto? = itemService.getAll(userId)

        // Then
        assertEquals(1, result?.listShortItems?.size)
    }

    @Test
    fun testUpdateItemMethod() = testApplication {
        // Given
        val itemId = 1
        val req = ItemUpdateReq(ItemCategory.TOPS, "Test Description", itemId)
        val itemEntity: ItemEntity = mockk()
        every { itemDao.getById(itemId) } returns itemEntity
        every { itemDao.update(any()) } returns Unit

        // When
        itemService.updateItem(itemId, req)

        // Then

        verify(exactly = 1) { itemDao.update(itemEntity) }
    }

    @Test
    fun testGetByCategoryMethod() = testApplication {
        // Given
        val categoryName = "TOPS"
        val userId = 1L
        val itemEntity: ItemEntity = mockk()
        val mockFileEntity = mockk<FileEntity>()

        every { itemDao.getByCategory(categoryName) } returns listOf(itemEntity)
        every { fileDao.getByItemId(any()) } returns mockFileEntity
        every { itemEntity.toItemShortDto(any()) } returns mockk()

        // When
        val result: ItemShortListDto? = itemService.getByCategory(categoryName, userId)

        // Then
        assertEquals(1, result?.listShortItems?.size)
    }
}
