package cl.yapo.test.domain.usecase

import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.request.SearchArtistsRequest
import cl.yapo.test.domain.repository.MusicLocalRepository
import cl.yapo.test.domain.repository.MusicRemoteRepository
import cl.yapo.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class SearchArtistsUseCase(
    private val cacheRepository: MusicLocalRepository,
    private val remoteRepository: MusicRemoteRepository
) : ResultUseCase<SearchArtistsRequest, List<Artist>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: SearchArtistsRequest): List<Artist>? {
        /* Get cache results from local cache */
        val cacheResults = cacheRepository.searchArtists(term = params.query)

        /* If there are cached results, return */
        if (cacheResults.isNotEmpty()) return cacheResults

        /* Get results from remote api */
        val remoteResults = remoteRepository.searchArtists(term = params.query)

        /* Save results to local cache */
        cacheRepository.saveArtistsSearch(artists = remoteResults, queryString = params.query)

        /* Return remote results */
        return remoteResults
    }
}