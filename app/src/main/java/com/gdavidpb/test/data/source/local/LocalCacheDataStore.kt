package com.gdavidpb.test.data.source.local

import android.content.Context
import com.gdavidpb.test.domain.repository.StorageRepository
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

class LocalCacheDataStore(
    context: Context
) : StorageRepository {

    private val root: File = context.filesDir

    override suspend fun get(name: String): File {
        return File(root, name)
    }

    override suspend fun exists(name: String): Boolean {
        val file = File(root, name)

        return file.exists()
    }

    override suspend fun inputStream(name: String): InputStream {
        val file = File(root, name)

        return file.inputStream()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun download(url: String, name: String): File {
        return URL(url).openStream().use { inputStream ->
            val file = File(root, name)

            file.parentFile?.mkdirs()

            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)

            outputStream.flush()
            outputStream.close()

            file
        }
    }

    override suspend fun delete(name: String) {
        val file = File(root, name)

        runCatching {
            with(file) { if (isDirectory) deleteRecursively() else delete() }
        }.onFailure { throwable ->
            if (throwable !is FileNotFoundException) throw throwable
        }
    }
}