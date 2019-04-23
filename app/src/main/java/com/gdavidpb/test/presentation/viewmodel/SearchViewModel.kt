package com.gdavidpb.test.presentation.viewmodel

import com.gdavidpb.test.domain.model.Artist
import com.gdavidpb.test.domain.model.request.LikeArtistsRequest
import com.gdavidpb.test.domain.model.request.SearchArtistsRequest
import com.gdavidpb.test.domain.usecase.LikeArtistUseCase
import com.gdavidpb.test.domain.usecase.SearchArtistsUseCase
import com.gdavidpb.test.domain.usecase.UnlikeArtistUseCase
import com.gdavidpb.test.presentation.state.SearchState
import com.gdavidpb.test.utils.LiveCompletable
import com.gdavidpb.test.utils.LiveResult
import com.gdavidpb.test.utils.postSuccess

open class SearchViewModel(
    private val searchArtistsUseCase: SearchArtistsUseCase,
    private val likeArtistUseCase: LikeArtistUseCase,
    private val unlikeArtistUseCase: UnlikeArtistUseCase
) : StateViewModel<SearchState>(initState = SearchState()) {
    val artists = LiveResult<List<Artist>>()
    val like = LiveCompletable()

    fun searchArtists(term: String) {
        val request = SearchArtistsRequest(query = term)

        searchArtistsUseCase.execute(liveData = artists, params = request)
    }

    fun resetSearch() {
        artists.postSuccess(listOf())
    }

    fun likeArtist(artistId: Long) {
        val request = LikeArtistsRequest(artistId = artistId)

        likeArtistUseCase.execute(liveData = like, params = request)
    }

    fun unlikeArtist(artistId: Long) {
        val request = LikeArtistsRequest(artistId = artistId)

        unlikeArtistUseCase.execute(liveData = like, params = request)
    }
}