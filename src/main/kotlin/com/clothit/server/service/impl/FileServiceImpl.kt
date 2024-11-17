package com.clothit.server.service.impl

import com.clothit.server.dao.FileDao
import com.clothit.server.model.entity.FileEntity
import com.clothit.server.service.FileService
import com.clothit.util.DateTimeUtil
import com.clothit.util.FileUtil
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class FileServiceImpl(
    private val fileDao: FileDao
) : FileService {

    override suspend fun save(file: MultiPartData): Int {
        var fileId: Int = -1

        withContext(Dispatchers.IO) {
            file.forEachPart { part ->
                if (part is PartData.FileItem) {
                    fileId = processAndSaveFilePart(part)
                }
            }
        }
        return fileId
    }

    override fun getById(fileId: Int): ByteArray {
        val fileEntity = fileDao.findById(fileId)
        return FileUtil.readFromFile(fileEntity.name)
    }


    override suspend fun update(fileId: Int, newFile: MultiPartData) {

        val oldFileEntity = fileDao.findById(fileId)
        val oldFileName = oldFileEntity.name
        val oldByteArray = FileUtil.readFromFile(oldFileName)

        withContext(Dispatchers.IO) {
            newFile.forEachPart { part ->
                if (part is PartData.FileItem) {
                    processFilePartUpdate(part, oldFileEntity, oldByteArray)
                }
            }
        }

    }

    /// --------- utils-------///

    //process file

    private fun processFilePartUpdate(
        part: PartData.FileItem,
        oldFileEntity: FileEntity,
        oldByteArray: ByteArray
    ) {

        val inputStream: InputStream = part.streamProvider()
        val newByteArray = inputStream.readBytes()

        if (oldByteArray.contentEquals(newByteArray)) {
            return
        }


        val newOriginalFileName = part.originalFileName!!
        FileUtil.saveToFile(newOriginalFileName, newByteArray)


        oldFileEntity.update(
            name = newOriginalFileName,
            size = newByteArray.size.toLong(),
            timeCreated = oldFileEntity.timeCreated,
            timeUpdated = DateTimeUtil.getCurrentTime()
        )

        fileDao.update(oldFileEntity)
    }

    private fun processAndSaveFilePart(part: PartData.FileItem): Int {
        val inputStream: InputStream = part.streamProvider()
        val byteArray = inputStream.readBytes()
        val originalFileName = part.originalFileName!!

        FileUtil.saveToFile(originalFileName, byteArray)

        val fileEntity = FileEntity.filePreSaveInit(name = originalFileName, size = byteArray.size.toLong())
        fileDao.save(fileEntity)
        return fileEntity.id!!
    }
}


