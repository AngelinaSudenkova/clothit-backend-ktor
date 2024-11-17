package com.clothit.server.api

import com.clothit.server.api.dto.IdDto
import com.clothit.server.service.FileService
import com.clothit.util.ObjectUtils
import io.ktor.http.content.*

class FileController(
    private val fileService: FileService
) {
    suspend fun save(req: MultiPartData): IdDto {
        val fileId = fileService.save(req)
        return IdDto(fileId)
    }

    fun getById(id: Int?): ByteArray {
        val validFileId = ObjectUtils.checkNotNullBadRequest(id)
        return fileService.getById(validFileId)
    }

    suspend fun update(fileId: Int?, req: MultiPartData) {
        val validFileId = ObjectUtils.checkNotNullBadRequest(fileId)
        fileService.update(validFileId, req)
    }
}