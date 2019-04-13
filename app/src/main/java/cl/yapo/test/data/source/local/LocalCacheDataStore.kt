package cl.yapo.test.data.source.local

import android.content.Context
import cl.yapo.test.domain.repository.StorageRepository
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream

open class LocalCacheDataStore(
    private val context: Context
) : StorageRepository {
    override suspend fun put(name: String, inputStream: InputStream): File {
        val outputFile = File(context.filesDir, name)

        /* Create directories to */
        outputFile.parentFile.mkdirs()

        val outputStream = FileOutputStream(outputFile)

        inputStream.copyTo(outputStream)

        outputStream.flush()
        outputStream.close()

        return outputFile
    }

    override suspend fun get(name: String): InputStream {
        val inputFile = File(context.filesDir, name)

        return inputFile.inputStream()
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