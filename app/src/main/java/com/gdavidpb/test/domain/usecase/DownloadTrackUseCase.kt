package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.request.DownloadTrackRequest
import com.gdavidpb.test.domain.model.response.DownloadTrackResponse
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.repository.StorageRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import com.gdavidpb.test.domain.usecase.errors.DownloadTrackError
import com.gdavidpb.test.utils.extensions.isConnectionIssue
import java.io.File
import java.io.IOException

class DownloadTrackUseCase(
    private val storageRepository: StorageRepository,
    private val musicRepository: MusicRepository,
    private val networkRepository: NetworkRepository
) : ResultUseCase<DownloadTrackRequest, DownloadTrackResponse, DownloadTrackError>() {
    override suspend fun executeOnBackground(params: DownloadTrackRequest): DownloadTrackResponse {
        val track = params.track

        val remoteFile = File(track.previewUrl.toString())
        val fileName = "${track.trackId}.${remoteFile.extension}"

        val isCached = storageRepository.exists(fileName)

        val localFile = if (isCached)
            storageRepository.get(fileName)
        else
            storageRepository.download(url = track.previewUrl.toString(), name = fileName).also {
                musicRepository.markTrackAsDownloaded(trackId = track.trackId)
            }

        return DownloadTrackResponse(track, localFile)
    }

    override suspend fun executeOnException(throwable: Throwable): DownloadTrackError? {
        return when {
            throwable is IOException -> DownloadTrackError.IO
            throwable.isConnectionIssue() -> DownloadTrackError.NoConnection(networkRepository.isAvailable())
            else -> null
        }
    }
}