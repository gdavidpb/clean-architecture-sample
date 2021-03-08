package com.gdavidpb.test.domain.repository

import java.io.File
import java.io.InputStream

interface StorageRepository {
    suspend fun get(name: String): File
    suspend fun exists(name: String): Boolean
    suspend fun inputStream(name: String): InputStream
    suspend fun download(url: String, name: String): File
    suspend fun delete(name: String)
}