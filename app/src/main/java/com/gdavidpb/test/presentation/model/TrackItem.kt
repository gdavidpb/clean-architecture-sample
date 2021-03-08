package com.gdavidpb.test.presentation.model

import androidx.annotation.DrawableRes
import com.gdavidpb.test.R

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
    @DrawableRes
    val nameIconResource: Int
) {
    fun computeTextColorResource() = when {
        isPlaying || isPaused || isLoading -> R.color.colorAccent
        else -> R.color.colorForegroundDark
    }

    fun computeActionIconResource() = when {
        isPlaying -> R.drawable.ic_pause
        isPaused -> R.drawable.ic_play_on
        isLoading -> R.drawable.ic_play
        isMusic -> R.drawable.ic_play
        isVideo -> R.drawable.ic_video
        else -> 0
    }
}