package com.gdavidpb.test.data.source.local

import android.net.ConnectivityManager
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.utils.extensions.isNetworkAvailable

class AndroidNetworkDataStore(
    private val connectivityManager: ConnectivityManager
) : NetworkRepository {
    override fun isAvailable(): Boolean {
        return connectivityManager.isNetworkAvailable()
    }
}