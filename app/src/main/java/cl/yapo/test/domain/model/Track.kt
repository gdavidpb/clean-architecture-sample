package cl.yapo.test.domain.model

data class Track(
    val trackId: Long,
    val trackName: String,
    val trackCensoredName: String,
    val trackViewUrl: String,
    val previewUrl: String,
    val trackPrice: Double,
    val trackExplicitness: String,
    val trackNumber: Int,
    val trackTimeMillis: Long,
    val currency: String,
    val primaryGenreName: String,
    val isStreamable: Boolean
)