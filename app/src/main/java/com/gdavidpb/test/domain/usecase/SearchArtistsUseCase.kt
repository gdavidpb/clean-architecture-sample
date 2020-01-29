package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.request.SearchArtistsRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class SearchArtistsUseCase(
    private val musicRepository: MusicRepository
) : ResultUseCase<SearchArtistsRequest, List<Artist>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: SearchArtistsRequest): List<Artist>? {
        return musicRepository.searchArtists(term = params.query)
    }
}