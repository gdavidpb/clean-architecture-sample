package cl.yapo.test.domain.repository

import cl.yapo.test.domain.model.Album
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.Track

interface MusicRemoteRepository {
    suspend fun searchArtists(term: String): List<Artist>
    suspend fun lookupAlbums(artistId: Long): List<Album>
    suspend fun lookupTracks(albumId: Long): List<Track>
}
