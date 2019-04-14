package cl.yapo.test.presentation.viewmodel

import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.request.LikeArtistsRequest
import cl.yapo.test.domain.model.request.SearchArtistsRequest
import cl.yapo.test.domain.usecase.LikeArtistUseCase
import cl.yapo.test.domain.usecase.SearchArtistsUseCase
import cl.yapo.test.domain.usecase.UnlikeArtistUseCase
import cl.yapo.test.presentation.state.SearchState
import cl.yapo.test.utils.LiveCompletable
import cl.yapo.test.utils.LiveResult
import cl.yapo.test.utils.postSuccess

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