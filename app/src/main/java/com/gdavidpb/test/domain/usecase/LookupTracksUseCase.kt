package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.request.LookupTracksRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import com.gdavidpb.test.domain.usecase.errors.LookupTracksError
import com.gdavidpb.test.utils.extensions.isConnectionIssue

open class LookupTracksUseCase(
    private val musicRepository: MusicRepository,
    private val networkRepository: NetworkRepository
) : ResultUseCase<LookupTracksRequest, List<Track>, LookupTracksError>() {
    override suspend fun executeOnBackground(params: LookupTracksRequest): List<Track>? {
        return musicRepository.lookupTracks(albumId = params.albumId)
    }

    override suspend fun executeOnException(throwable: Throwable): LookupTracksError? {
        return when {
            throwable.isConnectionIssue() -> LookupTracksError.NoConnection(networkRepository.isAvailable())
            else -> null
        }
    }
}