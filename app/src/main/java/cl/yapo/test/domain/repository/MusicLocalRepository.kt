package cl.yapo.test.domain.repository

import cl.yapo.test.domain.model.Album
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.Track

interface MusicLocalRepository {
    suspend fun searchArtists(term: String): List<Artist>
    suspend fun lookupAlbums(artistId: Long): List<Album>
    suspend fun lookupTracks(albumId: Long): List<Track>

    suspend fun saveArtistsSearch(artists: List<Artist>)
    suspend fun saveAlbumsLookup(albums: List<Album>)
    suspend fun saveTracksLookup(tracks: List<Track>)

    suspend fun likeArtist(artistId: Long)
    suspend fun unlikeArtist(artistId: Long)
}
