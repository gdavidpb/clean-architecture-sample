package cl.yapo.test.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.yapo.test.data.model.database.ArtistEntity
import cl.yapo.test.utils.COLUMN_ARTIST_ID
import cl.yapo.test.utils.COLUMN_ARTIST_NAME
import cl.yapo.test.utils.COLUMN_LIKE
import cl.yapo.test.utils.TABLE_ARTISTS

@Dao
interface ArtistDao {
    @Query("SELECT * FROM $TABLE_ARTISTS WHERE '$COLUMN_ARTIST_NAME' LIKE '%' || :term || '%'")
    suspend fun searchArtists(term: String): List<ArtistEntity>

    @Insert
    suspend fun saveArtistsSearch(vararg artists: ArtistEntity)

    @Query("UPDATE $TABLE_ARTISTS SET '$COLUMN_LIKE' = 1 WHERE '$COLUMN_ARTIST_ID' = :artistId")
    suspend fun likeArtist(artistId: Long)

    @Query("UPDATE $TABLE_ARTISTS SET '$COLUMN_LIKE' = 0 WHERE '$COLUMN_ARTIST_ID' = :artistId")
    suspend fun unlikeArtist(artistId: Long)
}
