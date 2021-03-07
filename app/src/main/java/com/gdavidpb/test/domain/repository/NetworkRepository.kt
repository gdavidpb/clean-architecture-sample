package com.gdavidpb.test.domain.repository

interface NetworkRepository {
    fun isAvailable(): Boolean
}