package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.data.source.local.MusicDatabase
import com.gdavidpb.test.domain.model.request.DownloadTrackRequest
import com.gdavidpb.test.domain.model.response.DownloadTrackResponse
import com.gdavidpb.test.domain.repository.StorageRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers
import java.io.File

open class DownloadTrackUseCase(
    private val storageRepository: StorageRepository,
    private val musicDatabase: MusicDatabase
) : ResultUseCase<DownloadTrackRequest, DownloadTrackResponse>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: DownloadTrackRequest): DownloadTrackResponse? {
        val track = params.track

        val remoteFile = File(track.previewUrl)
        val fileName = "${track.trackId}.${remoteFile.extension}"
        val localFile = File("cache", fileName)

        /* If there is cache for this track */
        if (storageRepository.exists(fileName)) return DownloadTrackResponse(track, localFile)

        /* Download this track to cache */
        val downloadedFile = storageRepository.download(url = track.previewUrl, name = localFile.path)

        musicDatabase.tracks.markAsDownloaded(trackId = track.trackId)

        return DownloadTrackResponse(track, downloadedFile)
    }
}