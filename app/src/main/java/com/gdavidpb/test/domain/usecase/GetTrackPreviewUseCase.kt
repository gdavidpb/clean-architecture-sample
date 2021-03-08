package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.request.GetTrackPreviewRequest
import com.gdavidpb.test.domain.model.response.GetTrackPreviewResponse
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.repository.StorageRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import com.gdavidpb.test.domain.usecase.errors.GetTrackPreviewError
import com.gdavidpb.test.utils.extensions.isConnectionIssue
import java.io.File
import java.io.IOException

class GetTrackPreviewUseCase(
    private val storageRepository: StorageRepository,
    private val musicRepository: MusicRepository,
    private val networkRepository: NetworkRepository
) : ResultUseCase<GetTrackPreviewRequest, GetTrackPreviewResponse, GetTrackPreviewError>() {
    override suspend fun executeOnBackground(params: GetTrackPreviewRequest): GetTrackPreviewResponse {
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

        return GetTrackPreviewResponse(track, localFile)
    }

    override suspend fun executeOnException(throwable: Throwable): GetTrackPreviewError? {
        return when {
            throwable is IOException -> GetTrackPreviewError.IO
            throwable.isConnectionIssue() -> GetTrackPreviewError.NoConnection(networkRepository.isAvailable())
            else -> null
        }
    }
}