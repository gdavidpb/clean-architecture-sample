package com.gdavidpb.test.domain.usecase

import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.usecase.coroutines.ResultUseCase
import kotlinx.coroutines.Dispatchers

open class GetLikedArtistsUseCase(
    private val musicRepository: MusicRepository
) : ResultUseCase<Unit, List<Artist>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: Unit): List<Artist>? {
        return musicRepository.getLikedArtists()
    }
}