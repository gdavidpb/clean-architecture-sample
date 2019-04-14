package cl.yapo.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import cl.yapo.test.domain.model.Album
import cl.yapo.test.domain.model.request.LookupAlbumsRequest
import cl.yapo.test.domain.usecase.LookupAlbumsUseCase
import cl.yapo.test.utils.LiveResult

open class ArtistViewModel(
    private val lookupAlbumsUseCase: LookupAlbumsUseCase
) : ViewModel() {
    val albums = LiveResult<List<Album>>()

    fun lookupAlbums(artistId: Long) {
        lookupAlbumsUseCase.execute(liveData = albums, params = LookupAlbumsRequest(artistId = artistId))
    }
}