package cl.yapo.test.data.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.yapo.test.utils.TABLE_TRACKS

@Entity(tableName = TABLE_TRACKS)
data class TrackEntity(
    @PrimaryKey
    val trackId: Long,
    val artistId: Long,
    val collectionId: Long,
    val artistName: String,
    val collectionName: String,
    val trackName: String,
    val collectionCensoredName: String,
    val trackCensoredName: String,
    val artistViewUrl: String,
    val collectionViewUrl: String,
    val trackViewUrl: String,
    val previewUrl: String,
    val collectionPrice: Double,
    val trackPrice: Double,
    val releaseDate: String,
    val isCollectionExplicit: Int,
    val isTrackExplicit: Int,
    val discCount: Int,
    val discNumber: Int,
    val trackCount: Int,
    val trackNumber: Int,
    val trackTimeMillis: Long,
    val country: String,
    val currency: String,
    val primaryGenreName: String,
    val isStreamable: Int,
    val isDownloaded: Int
)