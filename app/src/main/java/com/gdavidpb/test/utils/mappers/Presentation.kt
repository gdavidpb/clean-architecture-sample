package com.gdavidpb.test.utils.mappers

import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.utils.extensions.formatInterval

fun Track.toTrackItem() = TrackItem(
    artistId = artistId,
    collectionId = collectionId,
    trackId = trackId,
    artistName = artistName,
    collectionName = collectionName,
    trackName = trackName,
    collectionCensoredName = collectionCensoredName,
    trackCensoredName = trackCensoredName,
    artistViewUrl = artistViewUrl,
    collectionViewUrl = collectionViewUrl,
    trackViewUrl = trackViewUrl,
    previewUrl = previewUrl,
    collectionPrice = collectionPrice,
    trackPrice = trackPrice,
    releaseDate = releaseDate,
    isCollectionExplicit = isCollectionExplicit,
    isTrackExplicit = isTrackExplicit,
    discCount = discCount,
    discNumber = discNumber,
    trackCount = trackCount,
    trackNumber = trackNumber,
    trackTimeMillis = trackTimeMillis,
    country = country,
    currency = currency,
    primaryGenreName = primaryGenreName,
    isStreamable = isStreamable,
    isDownloading = isDownloading,
    isPlaying = isPlaying,
    isPaused = isPaused,
    isDownloaded = isDownloaded,
    isMusic = isMusic,
    isVideo = isVideo,
    /* Presentation fields */
    nameIconResource = if (isTrackExplicit) R.drawable.ic_explicit else 0,
    timeMillisString = trackTimeMillis.formatInterval()
)