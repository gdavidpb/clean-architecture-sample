package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.request.LikeArtistsRequest
import com.gdavidpb.test.domain.usecase.GetLikedArtistsUseCase
import com.gdavidpb.test.domain.usecase.UnlikeArtistUseCase
import com.gdavidpb.test.utils.LiveCompletable
import com.gdavidpb.test.utils.LiveResult

open class FavoritesViewModel(
    private val getLikedArtistsUseCase: GetLikedArtistsUseCase,
    private val unlikeArtistUseCase: UnlikeArtistUseCase
) : ViewModel() {
    val liked = LiveResult<List<Artist>>()
    val like = LiveCompletable()

    fun getLikedArtists() {
        getLikedArtistsUseCase.execute(liveData = liked, params = Unit)
    }

    fun unlikeArtist(artistId: Long) {
        val request = LikeArtistsRequest(artistId = artistId)

        unlikeArtistUseCase.execute(liveData = like, params = request)
    }
}