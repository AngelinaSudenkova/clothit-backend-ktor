package com.clothit.server.api

import com.clothit.server.api.dto.IdDto
import com.clothit.server.service.FileService
import io.ktor.http.content.*

class FileController(
    private val fileService: FileService
) {
    suspend fun save(req: MultiPartData): IdDto {
        val fileId = fileService.save(req)
        return IdDto(fileId)
    }

    fun getById(id: Int): ByteArray {
        return fileService.getById(id)
    }

    suspend fun update(fileId: Int, req: MultiPartData) {
        fileService.update(fileId, req)
    }
}