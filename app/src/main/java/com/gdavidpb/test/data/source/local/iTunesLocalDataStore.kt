package com.gdavidpb.test.data.source.local

import com.gdavidpb.test.data.repository.iTunesDataStore
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.utils.extensions.normalize
import com.gdavidpb.test.utils.mappers.*

class iTunesLocalDataStore(
    private val musicDatabase: MusicDatabase
) : iTunesDataStore {
    override suspend fun searchArtists(term: String): List<Artist> {
        return musicDatabase.artists.searchArtists(term = term.normalize()).map { it.toArtist() }
    }

    override suspend fun getLikedArtists(): List<Artist> {
        return musicDatabase.artists.getLikedArtists().map { it.toArtist() }
    }

    override suspend fun lookupAlbums(artistId: Long): List<Album> {
        return musicDatabase.albums.lookupAlbums(artistId = artistId).map { it.toAlbum() }
    }

    override suspend fun lookupTracks(albumId: Long): List<Track> {
        return musicDatabase.tracks.lookupTracks(albumId = albumId).map { it.toTrack() }
    }

    override suspend fun saveArtistsSearch(artists: List<Artist>, queryString: String) {
        val array = artists.map { it.toArtistEntity(queryString) }.toTypedArray()

        musicDatabase.artists.saveArtistsSearch(artists = array)
    }

    override suspend fun saveAlbumsLookup(albums: List<Album>) {
        val array = albums.map { it.toAlbumEntity() }.toTypedArray()

        musicDatabase.albums.saveAlbumsLookup(albums = array)
    }

    override suspend fun saveTracksLookup(tracks: List<Track>) {
        val array = tracks.map { it.toTrackEntity() }.toTypedArray()

        musicDatabase.tracks.saveTracksLookup(tracks = array)
    }

    override suspend fun likeArtist(artistId: Long) {
        musicDatabase.artists.likeArtist(artistId = artistId)
    }

    override suspend fun unlikeArtist(artistId: Long) {
        musicDatabase.artists.unlikeArtist(artistId = artistId)
    }

    override suspend fun markTrackAsDownloaded(trackId: Long) {
        musicDatabase.tracks.markAsDownloaded(trackId = trackId)
    }
}