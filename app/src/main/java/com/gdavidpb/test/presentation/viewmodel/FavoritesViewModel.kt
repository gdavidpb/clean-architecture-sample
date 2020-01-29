package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.request.LikeArtistsRequest
import com.gdavidpb.test.domain.usecase.GetLikedArtistsUseCase
import com.gdavidpb.test.domain.usecase.UnlikeArtistUseCase
import com.gdavidpb.test.utils.LiveCompletable
import com.gdavidpb.test.utils.LiveResult
import com.gdavidpb.test.utils.execute

open class FavoritesViewModel(
    private val getLikedArtistsUseCase: GetLikedArtistsUseCase,
    private val unlikeArtistUseCase: UnlikeArtistUseCase
) : ViewModel() {
    val liked = LiveResult<List<Artist>>()
    val like = LiveCompletable()

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