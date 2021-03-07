package com.gdavidpb.test.domain.usecase.errors

sealed class DownloadTrackError {
    object IO : DownloadTrackError()
    class NoConnection(val isNetworkAvailable: Boolean) : DownloadTrackError()
}