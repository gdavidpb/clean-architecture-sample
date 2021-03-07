package com.gdavidpb.test.domain.usecase.errors

sealed class LookupTracksError {
    class NoConnection(val isNetworkAvailable: Boolean) : LookupTracksError()
}