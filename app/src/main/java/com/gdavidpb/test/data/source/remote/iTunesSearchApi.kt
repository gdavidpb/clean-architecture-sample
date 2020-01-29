package com.gdavidpb.test.data.source.remote

import com.gdavidpb.test.data.model.api.AlbumEntry
import com.gdavidpb.test.data.model.api.ArtistEntry
import com.gdavidpb.test.data.model.api.SearchResult
import com.gdavidpb.test.data.model.api.TrackEntry
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesSearchApi {
    @GET("search")
    suspend fun searchArtists(
        @Query("term") terms: String,
        @Query("entity") entity: String = "musicArtist"
    ): Response<SearchResult<ArtistEntry>>

    @GET("lookup")
    suspend fun lookupAlbums(
        @Query("id") artistId: Long,
        @Query("entity") entity: String = "album"
    ): Response<SearchResult<AlbumEntry>>

    @GET("lookup")
    suspend fun lookupTracks(
        @Query("id") albumId: Long,
        @Query("entity") entity: String = "song"
    ): Response<SearchResult<TrackEntry>>
}