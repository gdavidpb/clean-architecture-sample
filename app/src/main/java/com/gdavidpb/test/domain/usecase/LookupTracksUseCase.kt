package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.request.LookupTracksRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class LookupTracksUseCase(
    private val musicRepository: MusicRepository
) : ResultUseCase<LookupTracksRequest, List<Track>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: LookupTracksRequest): List<Track>? {
        return musicRepository.lookupTracks(albumId = params.albumId)
    }
}