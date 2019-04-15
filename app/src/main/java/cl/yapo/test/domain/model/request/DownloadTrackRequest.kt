package cl.yapo.test.domain.model.request

import cl.yapo.test.domain.model.Track

data class DownloadTrackRequest(
    val track: Track
)