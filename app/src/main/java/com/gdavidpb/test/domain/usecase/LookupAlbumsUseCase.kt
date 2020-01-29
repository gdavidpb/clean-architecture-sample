package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.request.LookupAlbumsRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class LookupAlbumsUseCase(
    private val musicRepository: MusicRepository
    ) : ResultUseCase<LookupAlbumsRequest, List<Album>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: LookupAlbumsRequest): List<Album>? {
        return musicRepository.lookupAlbums(artistId = params.artistId)
    }
}