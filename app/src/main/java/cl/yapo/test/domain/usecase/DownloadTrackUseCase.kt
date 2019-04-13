package cl.yapo.test.domain.usecase

import cl.yapo.test.domain.model.request.DownloadTrackRequest
import cl.yapo.test.domain.repository.StorageRepository
import cl.yapo.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers
import java.io.File

open class DownloadTrackUseCase(
    private val storageRepository: StorageRepository
) : ResultUseCase<DownloadTrackRequest, File>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: DownloadTrackRequest): File? {
        val remoteFile = File(params.trackUrl)
        val fileName = "${params.trackId}.${remoteFile.extension}"
        val localFile = File("cache", fileName)

        /* If there is cache for this track */
        if (storageRepository.exists(fileName)) return localFile

        /* Download this track to cache */
        return storageRepository.download(url = params.trackUrl, name = localFile.path)
    }
}