package cl.yapo.test.domain.model.response

import cl.yapo.test.domain.model.Track
import java.io.File

data class DownloadTrackResponse(
    val track: Track,
    val file: File
)