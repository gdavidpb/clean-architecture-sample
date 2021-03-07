package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import com.gdavidpb.test.domain.usecase.errors.GetLikedArtistsError
import com.gdavidpb.test.utils.extensions.isConnectionIssue

open class GetLikedArtistsUseCase(
    private val musicRepository: MusicRepository,
    private val networkRepository: NetworkRepository
) : ResultUseCase<Unit, List<Artist>, GetLikedArtistsError>() {
    override suspend fun executeOnBackground(params: Unit): List<Artist>? {
        return musicRepository
            .getLikedArtists()
            .sortedBy { it.artistName }
    }

    override suspend fun executeOnException(throwable: Throwable): GetLikedArtistsError? {
        return when {
            throwable.isConnectionIssue() -> GetLikedArtistsError.NoConnection(networkRepository.isAvailable())
            else -> null
        }
    }
}