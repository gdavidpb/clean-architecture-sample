package cl.yapo.test.data.source.remote

import cl.yapo.test.domain.model.Album
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.Track
import cl.yapo.test.domain.repository.MusicRemoteRepository
import cl.yapo.test.utils.await
import cl.yapo.test.utils.toAlbum
import cl.yapo.test.utils.toArtist
import cl.yapo.test.utils.toTrack

open class MusicRemoteDataStore(
    private val iTunesSearchApi: iTunesSearchApi
) : MusicRemoteRepository {
    override suspend fun searchArtists(term: String): List<Artist> {
        val response = iTunesSearchApi.searchArtists(term).await()!!

        return response.results.map { it.toArtist() }
    }

    override suspend fun lookupAlbums(artistId: Long): List<Album> {
        val response = iTunesSearchApi.lookupAlbums(artistId).await()!!

        return response.results.drop(1).map { it.toAlbum() }
    }

    override suspend fun lookupTracks(albumId: Long): List<Track> {
        val response = iTunesSearchApi.lookupTracks(albumId).await()!!

        return response.results.drop(1).map { it.toTrack() }
    }
}