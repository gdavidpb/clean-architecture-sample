package com.gdavidpb.test.domain.model.response

import com.gdavidpb.test.presentation.model.TrackItem
import java.io.File

data class GetTrackPreviewResponse(
    val item: TrackItem,
    val trackFile: File
)