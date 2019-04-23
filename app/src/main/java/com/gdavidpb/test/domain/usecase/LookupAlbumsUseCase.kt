package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.request.LookupAlbumsRequest
import com.gdavidpb.test.domain.repository.MusicLocalRepository
import com.gdavidpb.test.domain.repository.MusicRemoteRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class LookupAlbumsUseCase(
    private val cacheRepository: MusicLocalRepository,
    private val remoteRepository: MusicRemoteRepository
    ) : ResultUseCase<LookupAlbumsRequest, List<Album>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: LookupAlbumsRequest): List<Album>? {
        /* Get cache results from local cache */
        val cacheResults = cacheRepository.lookupAlbums(artistId = params.artistId)

        /* If there are cached results, return */
        if (cacheResults.isNotEmpty()) return cacheResults

        /* Get results from remote api */
        val remoteResults = remoteRepository.lookupAlbums(artistId = params.artistId)

        /* Save results to local cache */
        cacheRepository.saveAlbumsLookup(albums = remoteResults)

        /* Return remote results */
        return remoteResults
    }
}