package com.gdavidpb.test.data.source

import com.gdavidpb.test.data.repository.iTunesDataStore
import com.gdavidpb.test.data.source.local.iTunesLocalDataStore
import com.gdavidpb.test.data.source.remote.iTunesRemoteDataStore

class iTunesDataStoreFactory(
    private val localDataStore: iTunesLocalDataStore,
    private val remoteDataStore: iTunesRemoteDataStore
) {
    fun retrieveLocalDataStore(): iTunesDataStore = localDataStore
    fun retrieveRemoteDataStore(): iTunesDataStore = remoteDataStore
}