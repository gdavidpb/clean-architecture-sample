package cl.yapo.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import cl.yapo.test.domain.usecase.*

open class MainViewModel(
    private val downloadTrackUseCase: DownloadTrackUseCase,
    private val likeArtistUseCase: LikeArtistUseCase,
    private val unlikeArtistUseCase: UnlikeArtistUseCase,
    private val lookupAlbumsUseCase: LookupAlbumsUseCase,
    private val lookupTracksUseCase: LookupTracksUseCase,
    private val searchArtistsUseCase: SearchArtistsUseCase
) : ViewModel() {

}