package cl.yapo.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import cl.yapo.test.domain.model.Artist
import cl.yapo.test.domain.model.request.LikeArtistsRequest
import cl.yapo.test.domain.usecase.GetLikedArtistsUseCase
import cl.yapo.test.domain.usecase.UnlikeArtistUseCase
import cl.yapo.test.utils.LiveCompletable
import cl.yapo.test.utils.LiveResult

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