package cl.yapo.test.domain.usecase

import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.repository.MusicLocalRepository
import cl.yapo.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class GetLikedArtistsUseCase(
    private val cacheRepository: MusicLocalRepository
) : ResultUseCase<Unit, List<Artist>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: Unit): List<Artist>? {
        return cacheRepository.getLikedArtists()
    }
}