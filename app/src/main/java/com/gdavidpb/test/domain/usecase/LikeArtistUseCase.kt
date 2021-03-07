package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.request.LikeArtistsRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.usecase.coroutines.CompletableUseCase
import com.gdavidpb.test.domain.usecase.errors.LikeArtistError
import com.gdavidpb.test.utils.extensions.isConnectionIssue

open class LikeArtistUseCase(
    private val musicRepository: MusicRepository,
    private val networkRepository: NetworkRepository
) : CompletableUseCase<LikeArtistsRequest, LikeArtistError>() {
    override suspend fun executeOnBackground(params: LikeArtistsRequest) {
        musicRepository.likeArtist(artistId = params.artistId)
    }

    override suspend fun executeOnException(throwable: Throwable): LikeArtistError? {
        return when {
            throwable.isConnectionIssue() -> LikeArtistError.NoConnection(networkRepository.isAvailable())
            else -> null
        }
    }
}