package com.gdavidpb.test.utils.mappers

import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.utils.extensions.formatInterval

fun Track.toTrackItem() = TrackItem(
    trackId = trackId,
    trackName = trackName,
    previewUrl = previewUrl,
    isLoading = isDownloading,
    isPlaying = isPlaying,
    isPaused = isPaused,
    isDownloaded = isDownloaded,
    isMusic = isMusic,
    isVideo = isVideo,
    /* Presentation fields */
    trackTimeMillis = trackTimeMillis.formatInterval(),
    nameIconResource = if (isTrackExplicit) R.drawable.ic_explicit else 0
)