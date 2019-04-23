package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.request.DownloadTrackRequest
import com.gdavidpb.test.domain.model.request.LookupTracksRequest
import com.gdavidpb.test.domain.model.response.DownloadTrackResponse
import com.gdavidpb.test.domain.usecase.DownloadTrackUseCase
import com.gdavidpb.test.domain.usecase.LookupTracksUseCase
import com.gdavidpb.test.utils.LiveResult

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