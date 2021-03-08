package com.gdavidpb.test.domain.usecase.errors

sealed class GetTrackPreviewError {
    object IO : GetTrackPreviewError()
    class NoConnection(val isNetworkAvailable: Boolean) : GetTrackPreviewError()
}