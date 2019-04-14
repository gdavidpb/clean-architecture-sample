package cl.yapo.test.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.yapo.test.data.model.database.AlbumEntity
import cl.yapo.test.utils.COLUMN_ARTIST_ID
import cl.yapo.test.utils.TABLE_ALBUMS

@Dao
interface AlbumDao {
    @Query("SELECT * FROM $TABLE_ALBUMS WHERE '$COLUMN_ARTIST_ID' = :artistId")
    suspend fun lookupAlbums(artistId: Long): List<AlbumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlbumsLookup(vararg albums: AlbumEntity)
}
