package cl.yapo.test.domain.model.request

data class DownloadTrackRequest(
    val trackId: Long,
    val trackUrl: String
)