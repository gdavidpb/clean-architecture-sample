package cl.yapo.test.data.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.yapo.test.utils.TABLE_ARTISTS

@Entity(tableName = TABLE_ARTISTS)
data class ArtistEntity(
    @PrimaryKey
    val artistId: Long,
    val artistName: String,
    val artistLinkUrl: String,
    val primaryGenreName: String,
    val primaryGenreId: Long,
    val like: Int,
    val queryString: String
)