package cl.yapo.test.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.yapo.test.data.model.database.TrackEntity
import cl.yapo.test.utils.COLUMN_COLLECTION_ID
import cl.yapo.test.utils.TABLE_TRACKS

@Dao
interface TrackDao {
    @Query("SELECT * FROM $TABLE_TRACKS WHERE '$COLUMN_COLLECTION_ID' = :albumId")
    suspend fun lookupTracks(albumId: Long): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTracksLookup(vararg tracks: TrackEntity)
}
