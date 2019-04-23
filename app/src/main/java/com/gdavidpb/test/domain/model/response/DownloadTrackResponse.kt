package com.gdavidpb.test.domain.model.response

import com.gdavidpb.test.domain.model.Track
import java.io.File

data class DownloadTrackResponse(
    val track: Track,
    val file: File
)