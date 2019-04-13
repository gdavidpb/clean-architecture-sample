package cl.yapo.test.data.source.remote

import cl.yapo.test.data.model.api.AlbumEntry
import cl.yapo.test.data.model.api.ArtistEntry
import cl.yapo.test.data.model.api.SearchResult
import cl.yapo.test.data.model.api.TrackEntry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesSearchApi {
    @GET("search")
    fun searchArtists(
        @Query("term") terms: String,
        @Query("entity") entity: String = "musicArtist"
    ): Call<SearchResult<ArtistEntry>>

    @GET("lookup")
    fun lookupAlbums(
        @Query("id") artistId: Long,
        @Query("entity") entity: String = "album"
    ): Call<SearchResult<AlbumEntry>>

    @GET("lookup")
    fun lookupTracks(
        @Query("id") albumId: Long,
        @Query("entity") entity: String = "song"
    ): Call<SearchResult<TrackEntry>>
}