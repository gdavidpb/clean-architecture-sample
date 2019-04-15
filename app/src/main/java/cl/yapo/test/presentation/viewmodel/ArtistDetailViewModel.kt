package cl.yapo.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import cl.yapo.test.domain.model.Track
import cl.yapo.test.domain.model.request.DownloadTrackRequest
import cl.yapo.test.domain.model.request.LookupTracksRequest
import cl.yapo.test.domain.model.response.DownloadTrackResponse
import cl.yapo.test.domain.usecase.DownloadTrackUseCase
import cl.yapo.test.domain.usecase.LookupTracksUseCase
import cl.yapo.test.utils.LiveResult

open class ArtistDetailViewModel(
    private val lookupTracksUseCase: LookupTracksUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase
) : ViewModel() {
    val tracks = LiveResult<List<Track>>()
    val download = LiveResult<DownloadTrackResponse>()

    fun lookupTracks(albumId: Long) {
        lookupTracksUseCase.execute(liveData = tracks, params = LookupTracksRequest(albumId = albumId))
    }

    fun downloadTrack(track: Track) {
        downloadTrackUseCase.execute(
            liveData = download,
            params = DownloadTrackRequest(track = track)
        )
    }
}