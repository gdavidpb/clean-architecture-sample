package cl.yapo.test.domain.model

import java.util.*

data class Album(
    val collectionId: Long,
    val artistId: Long,
    val artistName: String,
    val collectionName: String,
    val collectionCensoredName: String,
    val artistViewUrl: String,
    val collectionViewUrl: String,
    val artworkUrl30: String,
    val artworkUrl60: String,
    val artworkUrl100: String,
    val collectionPrice: Double,
    val isExplicit: Boolean,
    val trackCount: Int,
    val copyright: String,
    val country: String,
    val currency: String,
    val releaseDate: Date,
    val primaryGenreName: String
) {
    override fun hashCode() = collectionId.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album

        return (collectionId == other.collectionId)
    }
}