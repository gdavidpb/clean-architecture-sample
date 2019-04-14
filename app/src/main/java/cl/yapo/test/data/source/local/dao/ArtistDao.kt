package cl.yapo.test.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.yapo.test.data.model.database.ArtistEntity

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists WHERE artistName LIKE ('%' || :term || '%') OR queryString LIKE ('%' || :term || '%')")
    suspend fun searchArtists(term: String): List<ArtistEntity>

    @Query("SELECT * FROM artists WHERE `like` = 1")
    suspend fun getLikedArtists(): List<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArtistsSearch(vararg artists: ArtistEntity)

    @Query("UPDATE artists SET `like` = 1 WHERE artistId = :artistId")
    suspend fun likeArtist(artistId: Long)

    @Query("UPDATE artists SET `like` = 0 WHERE artistId = :artistId")
    suspend fun unlikeArtist(artistId: Long)
}
