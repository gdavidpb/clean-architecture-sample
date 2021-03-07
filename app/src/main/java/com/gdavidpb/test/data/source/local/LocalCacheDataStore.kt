package com.gdavidpb.test.data.source.local

import android.content.Context
import com.gdavidpb.test.domain.repository.StorageRepository
import java.io.*
import java.net.URL

class LocalCacheDataStore(
    private val context: Context
) : StorageRepository {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun download(url: String, name: String): File {
        return URL(url).openStream().use { inputStream ->
            val outputFile = File(context.filesDir, name)

            /* Create directories to */
            outputFile.parentFile?.mkdirs()
                ?: throw IOException("Unable to create directory for file '$outputFile'")

            val outputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)

            outputStream.flush()
            outputStream.close()

            outputFile
        }
    }

    override suspend fun get(name: String): InputStream {
        val inputFile = File(context.filesDir, name)

        return inputFile.inputStream()
    }

    override suspend fun exists(name: String): Boolean {
        return File(name).exists()
    }

    override suspend fun delete(name: String) {
        runCatching {
            File(context.filesDir, name).let {
                if (it.isDirectory)
                    it.deleteRecursively()
                else
                    it.delete()
            }
        }.onFailure { throwable ->
            if (throwable !is FileNotFoundException)
                throw throwable
        }
    }
}