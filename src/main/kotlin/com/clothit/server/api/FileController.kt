package com.clothit.server.api

import com.clothit.server.api.dto.IdDto
import com.clothit.server.dao.impl.FileDaoImpl
import com.clothit.server.service.FileService
import com.clothit.server.service.impl.FileServiceImpl
import io.ktor.http.content.*

class FileController(private val fileService: FileService = FileServiceImpl(FileDaoImpl)) {
    suspend fun save(req: MultiPartData): IdDto {
        val fileId = fileService.save(req)
        return IdDto(fileId)
    }

    fun getById(id: Int) : ByteArray{
        return fileService.getById(id)
    }
}