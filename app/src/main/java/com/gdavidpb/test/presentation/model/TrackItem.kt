package com.gdavidpb.test.presentation.model

import androidx.annotation.DrawableRes

data class TrackItem(
    val trackId: Long,
    val trackName: CharSequence,
    val previewUrl: CharSequence,
    val trackTimeMillis: CharSequence,
    val isLoading: Boolean,
    val isPlaying: Boolean,
    val isPaused: Boolean,
    val isDownloaded: Boolean,
    val isMusic: Boolean,
    val isVideo: Boolean,
    @DrawableRes val nameIconResource: Int
)