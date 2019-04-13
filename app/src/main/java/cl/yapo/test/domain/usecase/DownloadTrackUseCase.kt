package cl.yapo.test.domain.usecase

import cl.yapo.test.domain.model.request.DownloadTrackRequest
import cl.yapo.test.domain.repository.StorageRepository
import cl.yapo.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.net.URL

open class DownloadTrackUseCase(
    private val storageRepository: StorageRepository
) : ResultUseCase<DownloadTrackRequest, File>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: DownloadTrackRequest): File? {
        val remoteFile = File(params.trackUrl)
        val fileName = "${params.trackId}.${remoteFile.extension}"

        return URL(params.trackUrl).openStream().use { remoteStream ->
            storageRepository.put(name = fileName, inputStream = remoteStream)
        }
    }
}