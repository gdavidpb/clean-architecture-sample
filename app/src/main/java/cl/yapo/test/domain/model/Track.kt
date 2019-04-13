package cl.yapo.test.domain.model

data class Track(
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
    val trackViewUrl: String
)