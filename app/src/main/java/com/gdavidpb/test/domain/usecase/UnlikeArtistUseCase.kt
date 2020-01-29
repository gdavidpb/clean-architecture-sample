package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.request.LikeArtistsRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.usecase.coroutines.CompletableUseCase
import kotlinx.coroutines.Dispatchers

open class UnlikeArtistUseCase(
    private val musicRepository: MusicRepository
) : CompletableUseCase<LikeArtistsRequest>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: LikeArtistsRequest) {
        musicRepository.unlikeArtist(artistId = params.artistId)
    }
}