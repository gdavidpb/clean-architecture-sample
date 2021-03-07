package com.gdavidpb.test.domain.usecase.errors

sealed class SearchArtistsError {
    class NoConnection(val isNetworkAvailable: Boolean) : SearchArtistsError()
}