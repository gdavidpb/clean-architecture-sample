package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.request.LookupTracksRequest
import com.gdavidpb.test.domain.repository.MusicLocalRepository
import com.gdavidpb.test.domain.repository.MusicRemoteRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
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