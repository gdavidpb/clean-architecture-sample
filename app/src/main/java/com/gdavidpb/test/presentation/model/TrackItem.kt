package com.gdavidpb.test.presentation.model

import com.gdavidpb.test.R

data class TrackItem(
    val artistId: Long,
    val collectionId: Long,
    val trackId: Long,
    val artistName: String,
    val collectionName: String,
    val trackName: String,
    val collectionCensoredName: String,
    val trackCensoredName: String,
    val artistViewUrl: String,
    val collectionViewUrl: String,
    val trackViewUrl: String,
    val previewUrl: String,
    val collectionPrice: Double,
    val trackPrice: Double,
    val releaseDate: String,
    val isCollectionExplicit: Boolean,
    val isTrackExplicit: Boolean,
    val discCount: Int,
    val discNumber: Int,
    val trackCount: Int,
    val trackNumber: Int,
    val trackTimeMillis: Long,
    val country: String,
    val currency: String,
    val primaryGenreName: String,
    val isStreamable: Boolean,
    val isDownloading: Boolean,
    val isPlaying: Boolean,
    val isPaused: Boolean,
    val isDownloaded: Boolean,
    val isMusic: Boolean,
    val isVideo: Boolean,
    val nameIconResource: Int,
    val timeMillisString: String
) {
    override fun hashCode() = trackId.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrackItem

        return (trackId == other.trackId)
    }

    fun computeTextColorResource() = when {
        isPlaying || isPaused || isDownloading -> R.color.colorAccent
        else -> R.color.colorForegroundDark
    }

    fun computeActionIconResource() = when {
        isPlaying -> R.drawable.ic_pause
        isPaused -> R.drawable.ic_play_on
        isDownloading -> R.drawable.ic_play
        isMusic -> R.drawable.ic_play
        isVideo -> R.drawable.ic_video
        else -> 0
    }
}