package com.gdavidpb.test.domain.usecase.errors

sealed class GetLikedArtistsError {
    class NoConnection(val isNetworkAvailable: Boolean) : GetLikedArtistsError()
}