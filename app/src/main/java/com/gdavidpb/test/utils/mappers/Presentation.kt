package com.gdavidpb.test.utils.mappers

import android.content.Context
import com.gdavidpb.test.R
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.presentation.model.AlbumItem
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.utils.extensions.formatInterval
import com.gdavidpb.test.utils.extensions.formatYear

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

fun Album.toAlbumItem(context: Context) = AlbumItem(
    collectionId = collectionId,
    collectionName = collectionName,
    artistName = artistName,
    genreAndYear = context.getString(
        R.string.text_album_genre_year,
        primaryGenreName,
        releaseDate.formatYear()
    ),
    artworkUrl = artworkUrl100,
    nameIconResource = if (isExplicit) R.drawable.ic_explicit else 0
)