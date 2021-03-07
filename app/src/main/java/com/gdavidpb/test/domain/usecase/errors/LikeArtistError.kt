package com.gdavidpb.test.domain.usecase.errors

sealed class LikeArtistError {
    class NoConnection(val isNetworkAvailable: Boolean) : LikeArtistError()
}