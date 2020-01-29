package com.gdavidpb.test.domain.repository

import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.Track

interface MusicRepository {
    suspend fun searchArtists(term: String): List<Artist>
    suspend fun getLikedArtists(): List<Artist>

    suspend fun lookupAlbums(artistId: Long): List<Album>
    suspend fun lookupTracks(albumId: Long): List<Track>

    suspend fun saveArtistsSearch(artists: List<Artist>, queryString: String)
    suspend fun saveAlbumsLookup(albums: List<Album>)
    suspend fun saveTracksLookup(tracks: List<Track>)

    suspend fun likeArtist(artistId: Long)
    suspend fun unlikeArtist(artistId: Long)
}
