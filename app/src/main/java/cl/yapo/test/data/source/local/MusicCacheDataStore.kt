package cl.yapo.test.data.source.local

import cl.yapo.test.domain.model.Album
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.Track
import cl.yapo.test.domain.repository.MusicLocalRepository
import cl.yapo.test.utils.*

open class MusicCacheDataStore(
    private val musicDatabase: MusicDatabase
) : MusicLocalRepository {
    override suspend fun searchArtists(term: String): List<Artist> {
        return musicDatabase.artists().searchArtists(term = term).map { it.toArtist() }
    }

    override suspend fun lookupAlbums(artistId: Long): List<Album> {
        return musicDatabase.albums().lookupAlbums(artistId = artistId).map { it.toAlbum() }
    }

    override suspend fun lookupTracks(albumId: Long): List<Track> {
        return musicDatabase.tracks().lookupTracks(albumId = albumId).map { it.toTrack() }
    }

    override suspend fun saveArtistsSearch(artists: List<Artist>) {
        val array = artists.map { it.toArtistEntity() }.toTypedArray()

        musicDatabase.artists().saveArtistsSearch(artists = *array)
    }

    override suspend fun saveAlbumsLookup(albums: List<Album>) {
        val array = albums.map { it.toAlbumEntity() }.toTypedArray()

        musicDatabase.albums().saveAlbumsLookup(albums = *array)
    }

    override suspend fun saveTracksLookup(tracks: List<Track>) {
        val array = tracks.map { it.toTrackEntity() }.toTypedArray()

        musicDatabase.tracks().saveTracksLookup(tracks = *array)
    }

    override suspend fun likeArtist(artistId: Long) {
        musicDatabase.artists().likeArtist(artistId = artistId)
    }

    override suspend fun unlikeArtist(artistId: Long) {
        musicDatabase.artists().unlikeArtist(artistId = artistId)
    }
}