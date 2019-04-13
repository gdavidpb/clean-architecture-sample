package cl.yapo.test.domain.repository

import java.io.File
import java.io.InputStream

interface StorageRepository {
    suspend fun put(name: String, inputStream: InputStream): File
    suspend fun get(name: String): InputStream
    suspend fun delete(name: String)
}