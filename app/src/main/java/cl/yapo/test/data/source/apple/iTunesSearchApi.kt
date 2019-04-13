package cl.yapo.test.data.source.apple

import cl.yapo.test.data.model.AlbumEntry
import cl.yapo.test.data.model.ArtistEntry
import cl.yapo.test.data.model.SearchResult
import cl.yapo.test.data.model.TrackEntry
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