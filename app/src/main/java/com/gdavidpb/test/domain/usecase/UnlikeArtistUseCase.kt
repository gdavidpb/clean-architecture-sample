package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.request.LikeArtistsRequest
import com.gdavidpb.test.domain.repository.MusicLocalRepository
import com.gdavidpb.test.domain.usecase.coroutines.CompletableUseCase
import kotlinx.coroutines.Dispatchers

open class UnlikeArtistUseCase(
    private val cacheRepository: MusicLocalRepository
) : CompletableUseCase<LikeArtistsRequest>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: LikeArtistsRequest) {
        cacheRepository.unlikeArtist(artistId = params.artistId)
    }
}