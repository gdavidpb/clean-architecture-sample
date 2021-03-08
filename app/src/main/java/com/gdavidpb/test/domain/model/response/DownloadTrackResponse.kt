package com.gdavidpb.test.domain.model.response

import com.gdavidpb.test.presentation.model.TrackItem
import java.io.File

data class DownloadTrackResponse(
    val track: TrackItem,
    val file: File
)