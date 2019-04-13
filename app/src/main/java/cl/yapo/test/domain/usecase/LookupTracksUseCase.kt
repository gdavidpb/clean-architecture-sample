package cl.yapo.test.domain.usecase

import cl.yapo.test.domain.model.Track
import cl.yapo.test.domain.model.request.LookupTracksRequest
import cl.yapo.test.domain.repository.MusicLocalRepository
import cl.yapo.test.domain.repository.MusicRemoteRepository
import cl.yapo.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class LookupTracksUseCase(
    private val cacheRepository: MusicLocalRepository,
    private val remoteRepository: MusicRemoteRepository
) : ResultUseCase<LookupTracksRequest, List<Track>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: LookupTracksRequest): List<Track>? {
        /* Get cacheRepository results from local cacheRepository */
        val cacheResults = cacheRepository.lookupTracks(albumId = params.albumId)

        /* If there are cached results, return */
        if (cacheResults.isNotEmpty()) return cacheResults

        /* Get results from remoteRepository api */
        val remoteResults = remoteRepository.lookupTracks(albumId = params.albumId)

        /* Save results to local cacheRepository */
        cacheRepository.saveTracksLookup(tracks = remoteResults)

        /* Return remoteRepository results */
        return remoteResults
    }
}