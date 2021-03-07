package com.gdavidpb.test.data.source.remote

import com.gdavidpb.test.data.repository.iTunesDataStore
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.utils.extensions.getOrThrow
import com.gdavidpb.test.utils.mappers.toAlbum
import com.gdavidpb.test.utils.mappers.toArtist
import com.gdavidpb.test.utils.mappers.toTrack

class iTunesRemoteDataStore(
    private val iTunesSearchApi: iTunesSearchApi
) : iTunesDataStore {
    override suspend fun searchArtists(term: String): List<Artist> {
        val response = iTunesSearchApi.searchArtists(term).getOrThrow()

        return response.results.map { it.toArtist() }
    }

    override suspend fun lookupAlbums(artistId: Long): List<Album> {
        val response = iTunesSearchApi.lookupAlbums(artistId).getOrThrow()

        return response.results.drop(1).map { it.toAlbum() }
    }

    override suspend fun lookupTracks(albumId: Long): List<Track> {
        val response = iTunesSearchApi.lookupTracks(albumId).getOrThrow()

        return response.results.drop(1).map { it.toTrack() }
    }

    override suspend fun getLikedArtists(): List<Artist> {
        throw NotImplementedError()
    }

    override suspend fun saveArtistsSearch(artists: List<Artist>, queryString: String) {
        throw NotImplementedError()
    }

    override suspend fun saveAlbumsLookup(albums: List<Album>) {
        throw NotImplementedError()
    }

    override suspend fun saveTracksLookup(tracks: List<Track>) {
        throw NotImplementedError()
    }

    override suspend fun likeArtist(artistId: Long) {
        throw NotImplementedError()
    }

    override suspend fun unlikeArtist(artistId: Long) {
        throw NotImplementedError()
    }

    override suspend fun markTrackAsDownloaded(trackId: Long) {
        throw NotImplementedError()
    }
}