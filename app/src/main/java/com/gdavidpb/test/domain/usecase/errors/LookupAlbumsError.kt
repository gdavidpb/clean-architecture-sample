package com.gdavidpb.test.domain.usecase.errors

sealed class LookupAlbumsError {
    class NoConnection(val isNetworkAvailable: Boolean) : LookupAlbumsError()
}