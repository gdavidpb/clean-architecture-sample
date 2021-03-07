package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.request.LikeArtistsRequest
import com.gdavidpb.test.domain.usecase.GetLikedArtistsUseCase
import com.gdavidpb.test.domain.usecase.UnlikeArtistUseCase
import com.gdavidpb.test.domain.usecase.errors.GetLikedArtistsError
import com.gdavidpb.test.domain.usecase.errors.LikeArtistError
import com.gdavidpb.test.utils.extensions.LiveCompletable
import com.gdavidpb.test.utils.extensions.LiveResult
import com.gdavidpb.test.utils.extensions.execute

class FavoritesViewModel(
    private val getLikedArtistsUseCase: GetLikedArtistsUseCase,
    private val unlikeArtistUseCase: UnlikeArtistUseCase
) : ViewModel() {
    val liked = LiveResult<List<Artist>, GetLikedArtistsError>()
    val like = LiveCompletable<LikeArtistError>()

    fun getLikedArtists() = execute(
        useCase = getLikedArtistsUseCase,
        liveData = liked,
        params = Unit
    )

    fun unlikeArtist(artistId: Long) = execute(
        useCase = unlikeArtistUseCase,
        liveData = like,
        params = LikeArtistsRequest(artistId = artistId)
    )
}