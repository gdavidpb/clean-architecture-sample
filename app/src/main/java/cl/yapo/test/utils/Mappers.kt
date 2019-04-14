package cl.yapo.test.utils

import cl.yapo.test.data.model.api.AlbumEntry
import cl.yapo.test.data.model.api.ArtistEntry
import cl.yapo.test.data.model.api.TrackEntry
import cl.yapo.test.data.model.database.AlbumEntity
import cl.yapo.test.data.model.database.ArtistEntity
import cl.yapo.test.data.model.database.TrackEntity
import cl.yapo.test.domain.model.Album
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.Track
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
        trackViewUrl = trackViewUrl
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
        trackId = trackId,
        artistId = artistId,
        collectionId = collectionId,
        artistName = artistName,
        collectionName = collectionName,
        trackName = trackName,
        collectionCensoredName = collectionCensoredName,
        trackCensoredName = trackCensoredName,
        artistViewUrl = artistViewUrl,
        collectionViewUrl = collectionViewUrl,
        trackViewUrl = trackViewUrl
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
        trackId = trackId,
        artistId = artistId,
        collectionId = collectionId,
        artistName = artistName,
        collectionName = collectionName,
        trackName = trackName,
        collectionCensoredName = collectionCensoredName,
        trackCensoredName = trackCensoredName,
        artistViewUrl = artistViewUrl,
        collectionViewUrl = collectionViewUrl,
        trackViewUrl = trackViewUrl
    )
}