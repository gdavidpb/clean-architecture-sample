package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.request.SearchArtistsRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import com.gdavidpb.test.domain.usecase.errors.SearchArtistsError
import com.gdavidpb.test.utils.extensions.isConnectionIssue

open class SearchArtistsUseCase(
    private val musicRepository: MusicRepository,
    private val networkRepository: NetworkRepository
) : ResultUseCase<SearchArtistsRequest, List<Artist>, SearchArtistsError>() {
    override suspend fun executeOnBackground(params: SearchArtistsRequest): List<Artist>? {
        return musicRepository
            .searchArtists(term = params.query)
            .sortedBy { it.artistName }
    }

    override suspend fun executeOnException(throwable: Throwable): SearchArtistsError? {
        return when {
            throwable.isConnectionIssue() -> SearchArtistsError.NoConnection(networkRepository.isAvailable())
            else -> null
        }
    }
}