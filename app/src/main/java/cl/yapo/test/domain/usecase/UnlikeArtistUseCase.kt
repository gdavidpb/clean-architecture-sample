package cl.yapo.test.domain.usecase

import cl.yapo.test.domain.model.request.LikeArtistsRequest
import cl.yapo.test.domain.repository.MusicLocalRepository
import cl.yapo.test.domain.usecase.coroutines.CompletableUseCase
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