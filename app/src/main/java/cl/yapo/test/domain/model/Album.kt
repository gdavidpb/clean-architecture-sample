package cl.yapo.test.domain.model

data class Album(
    val collectionId: Long,
    val collectionName: String,
    val collectionCensoredName: String,
    val collectionViewUrl: String,
    val artworkUrl30: String,
    val artworkUrl60: String,
    val artworkUrl100: String,
    val collectionPrice: Double,
    val releaseDate: String,
    val collectionExplicitness: String,
    val discCount: Int,
    val discNumber: Int,
    val trackCount: Int,
    val country: String,
    val currency: String,
    val primaryGenreName: String
)