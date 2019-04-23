package com.gdavidpb.test.utils

import com.gdavidpb.test.data.model.api.AlbumEntry
import com.gdavidpb.test.data.model.api.ArtistEntry
import com.gdavidpb.test.data.model.api.TrackEntry
import com.gdavidpb.test.data.model.database.AlbumEntity
import com.gdavidpb.test.data.model.database.ArtistEntity
import com.gdavidpb.test.data.model.database.TrackEntity
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.Track
import java.util.*

/* from data to domain layer */

fun ArtistEntry.toArtist(): Artist {
    return Artist(
        artistId = artistId,
        artistName = artistName,
        artistLinkUrl = artistLinkUrl ?: "",
        primaryGenreName = primaryGenreName ?: "",
        primaryGenreId = primaryGenreId ?: 0,
        isLiked = false
    )
}

fun AlbumEntry.toAlbum(): Album {
    return Album(
        collectionId = collectionId,
        artistId = artistId,
        artistName = artistName,
        collectionName = collectionName,
        collectionCensoredName = collectionCensoredName,
        artistViewUrl = artistViewUrl,
        collectionViewUrl = collectionViewUrl,
        artworkUrl30 = artworkUrl30 ?: "",
        artworkUrl60 = artworkUrl60 ?: "",
        artworkUrl100 = artworkUrl100 ?: "",
        collectionPrice = collectionPrice,
        isExplicit = collectionExplicitness != "notExplicit",
        trackCount = trackCount,
        copyright = copyright,
        country = country,
        currency = currency,
        releaseDate = releaseDate.parseISO8601Date(),
        primaryGenreName = primaryGenreName
    )
}

fun TrackEntry.toTrack(): Track {
    return Track(
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
        isCollectionExplicit = collectionExplicitness != "notExplicit",
        isTrackExplicit = trackExplicitness != "notExplicit",
        discCount = discCount,
        discNumber = discNumber,
        trackCount = trackCount,
        trackNumber = trackNumber,
        trackTimeMillis = trackTimeMillis,
        country = country,
        currency = currency,
        primaryGenreName = primaryGenreName,
        isStreamable = isStreamable,
        isDownloading = false,
        isPlaying = false,
        isPaused = false,
        isDownloaded = false,
        isMusic = previewUrl.endsWith(".m4a"),
        isVideo = previewUrl.endsWith(".m4v")
    )
}

fun ArtistEntity.toArtist(): Artist {
    return Artist(
        artistId = artistId,
        artistName = artistName,
        artistLinkUrl = artistLinkUrl,
        primaryGenreName = primaryGenreName,
        primaryGenreId = primaryGenreId,
        isLiked = (like == 1)
    )
}

fun AlbumEntity.toAlbum(): Album {
    return Album(
        collectionId = collectionId,
        artistId = artistId,
        artistName = artistName,
        collectionName = collectionName,
        collectionCensoredName = collectionCensoredName,
        artistViewUrl = artistViewUrl,
        collectionViewUrl = collectionViewUrl,
        artworkUrl30 = artworkUrl30 ?: "",
        artworkUrl60 = artworkUrl60 ?: "",
        artworkUrl100 = artworkUrl100 ?: "",
        collectionPrice = collectionPrice,
        isExplicit = (isExplicit == 1),
        trackCount = trackCount,
        copyright = copyright,
        country = country,
        currency = currency,
        releaseDate = Date(releaseDate),
        primaryGenreName = primaryGenreName
    )
}

fun TrackEntity.toTrack(): Track {
    return Track(
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
        isCollectionExplicit = (isCollectionExplicit == 1),
        isTrackExplicit = (isTrackExplicit == 1),
        discCount = discCount,
        discNumber = discNumber,
        trackCount = trackCount,
        trackNumber = trackNumber,
        trackTimeMillis = trackTimeMillis,
        country = country,
        currency = currency,
        primaryGenreName = primaryGenreName,
        isStreamable = (isStreamable == 1),
        isDownloading = false,
        isPlaying = false,
        isPaused = false,
        isDownloaded = (isDownloaded == 1),
        isMusic = previewUrl.endsWith(".m4a"),
        isVideo = previewUrl.endsWith(".m4v")
    )
}

/* from domain to data layer */

fun Artist.toArtistEntity(queryString: String): ArtistEntity {
    return ArtistEntity(
        artistId = artistId,
        artistName = artistName,
        artistLinkUrl = artistLinkUrl,
        primaryGenreName = primaryGenreName,
        primaryGenreId = primaryGenreId,
        like = if (isLiked) 1 else 0,
        queryString = queryString
    )
}

fun Album.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        collectionId = collectionId,
        artistId = artistId,
        artistName = artistName,
        collectionName = collectionName,
        collectionCensoredName = collectionCensoredName,
        artistViewUrl = artistViewUrl,
        collectionViewUrl = collectionViewUrl,
        artworkUrl30 = artworkUrl30,
        artworkUrl60 = artworkUrl60,
        artworkUrl100 = artworkUrl100,
        collectionPrice = collectionPrice,
        isExplicit = if (isExplicit) 1 else 0,
        trackCount = trackCount,
        copyright = copyright,
        country = country,
        currency = currency,
        releaseDate = releaseDate.time,
        primaryGenreName = primaryGenreName
    )
}

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
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
        isCollectionExplicit = if (isCollectionExplicit) 1 else 0,
        isTrackExplicit = if (isTrackExplicit) 1 else 0,
        discCount = discCount,
        discNumber = discNumber,
        trackCount = trackCount,
        trackNumber = trackNumber,
        trackTimeMillis = trackTimeMillis,
        country = country,
        currency = currency,
        primaryGenreName = primaryGenreName,
        isStreamable = if (isStreamable) 1 else 0,
        isDownloaded = if (isDownloaded) 1 else 0
    )
}