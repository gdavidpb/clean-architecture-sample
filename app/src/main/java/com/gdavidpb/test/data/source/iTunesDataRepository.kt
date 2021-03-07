package com.gdavidpb.test.data.source

import com.gdavidpb.test.data.repository.iTunesDataStore
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.repository.MusicRepository

open class iTunesDataRepository(
    private val factory: iTunesDataStoreFactory
) : MusicRepository {
    override suspend fun searchArtists(term: String): List<Artist> {
        return cacheOperation(fetch = {
            searchArtists(term)
        }, save = { data ->
            saveArtistsSearch(artists = data, queryString = term)
        })
    }

    override suspend fun lookupAlbums(artistId: Long): List<Album> {
        return cacheOperation(fetch = {
            lookupAlbums(artistId = artistId)
        }, save = { data ->
            saveAlbumsLookup(albums = data)
        })
    }

    override suspend fun lookupTracks(albumId: Long): List<Track> {
        return cacheOperation(fetch = {
            lookupTracks(albumId = albumId)
        }, save = { data ->
            saveTracksLookup(tracks = data)
        })
    }

    override suspend fun getLikedArtists(): List<Artist> {
        val local = factory.retrieveLocalDataStore()

        return local.getLikedArtists()
    }

    override suspend fun saveArtistsSearch(artists: List<Artist>, queryString: String) {
        val local = factory.retrieveLocalDataStore()

        local.saveArtistsSearch(artists, queryString)
    }

    override suspend fun saveAlbumsLookup(albums: List<Album>) {
        val local = factory.retrieveLocalDataStore()

        local.saveAlbumsLookup(albums)
    }

    override suspend fun saveTracksLookup(tracks: List<Track>) {
        val local = factory.retrieveLocalDataStore()

        local.saveTracksLookup(tracks)
    }

    override suspend fun likeArtist(artistId: Long) {
        val local = factory.retrieveLocalDataStore()

        local.likeArtist(artistId)
    }

    override suspend fun unlikeArtist(artistId: Long) {
        val local = factory.retrieveLocalDataStore()

        local.unlikeArtist(artistId)
    }

    override suspend fun markTrackAsDownloaded(trackId: Long) {
        val local = factory.retrieveLocalDataStore()

        local.markTrackAsDownloaded(trackId)
    }

    /* Cache helper */
    private suspend fun <R> cacheOperation(
        fetch: suspend iTunesDataStore.() -> List<R>,
        save: suspend iTunesDataStore.(List<R>) -> Unit
    ): List<R> {
        val local = factory.retrieveLocalDataStore()
        val remote = factory.retrieveRemoteDataStore()

        /* Get cache results from local */
        val localData = local.fetch()

        /* If there are cached results, return */
        if (localData.isNotEmpty()) return localData

        /* Get results from remote */
        val remoteData = remote.fetch()

        /* Save results to local  */
        local.save(remoteData)

        /* Return remote */
        return remoteData
    }
}