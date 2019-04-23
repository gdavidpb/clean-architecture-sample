package com.gdavidpb.test.domain.repository

import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.Track

interface MusicRemoteRepository {
    suspend fun searchArtists(term: String): List<Artist>
    suspend fun lookupAlbums(artistId: Long): List<Album>
    suspend fun lookupTracks(albumId: Long): List<Track>
}
