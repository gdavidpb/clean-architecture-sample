package cl.yapo.test.domain.repository

import java.io.File
import java.io.InputStream

interface StorageRepository {
    suspend fun download(url: String, name: String): File
    suspend fun get(name: String): InputStream
    suspend fun exists(name: String): Boolean
    suspend fun delete(name: String)
}