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
import com.gdavidpb.test.utils.execute
import com.gdavidpb.test.utils.postSuccess

open class SearchViewModel(
    private val searchArtistsUseCase: SearchArtistsUseCase,
    private val likeArtistUseCase: LikeArtistUseCase,
    private val unlikeArtistUseCase: UnlikeArtistUseCase
) : StateViewModel<SearchState>(initState = SearchState()) {
    val artists = LiveResult<List<Artist>>()
    val like = LiveCompletable()

    fun resetSearch() {
        artists.postSuccess(listOf())
    }

    fun searchArtists(term: String) = execute(
        useCase = searchArtistsUseCase,
        liveData = artists,
        params = SearchArtistsRequest(query = term)
    )

    fun likeArtist(artistId: Long) = execute(
        useCase = likeArtistUseCase,
        liveData = like,
        params = LikeArtistsRequest(artistId = artistId)
    )

    fun unlikeArtist(artistId: Long) = execute(
        useCase = unlikeArtistUseCase,
        liveData = like,
        params = LikeArtistsRequest(artistId = artistId)
    )
}