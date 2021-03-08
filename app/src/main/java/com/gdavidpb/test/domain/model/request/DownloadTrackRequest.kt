package com.gdavidpb.test.domain.model.request

import com.gdavidpb.test.presentation.model.TrackItem

data class DownloadTrackRequest(
    val track: TrackItem
)