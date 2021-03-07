package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.request.LookupAlbumsRequest
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import com.gdavidpb.test.domain.usecase.errors.LookupAlbumsError
import com.gdavidpb.test.utils.extensions.isConnectionIssue

class LookupAlbumsUseCase(
    private val musicRepository: MusicRepository,
    private val networkRepository: NetworkRepository
) : ResultUseCase<LookupAlbumsRequest, List<Album>, LookupAlbumsError>() {
    override suspend fun executeOnBackground(params: LookupAlbumsRequest): List<Album>? {
        return musicRepository
            .lookupAlbums(artistId = params.artistId)
            .sortedByDescending { it.releaseDate }
    }

    override suspend fun executeOnException(throwable: Throwable): LookupAlbumsError? {
        return when {
            throwable.isConnectionIssue() -> LookupAlbumsError.NoConnection(networkRepository.isAvailable())
            else -> null
        }
    }
}