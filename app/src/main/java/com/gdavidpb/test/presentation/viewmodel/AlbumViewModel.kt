package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.request.DownloadTrackRequest
import com.gdavidpb.test.domain.model.request.LookupTracksRequest
import com.gdavidpb.test.domain.model.response.DownloadTrackResponse
import com.gdavidpb.test.domain.usecase.DownloadTrackUseCase
import com.gdavidpb.test.domain.usecase.LookupTracksUseCase
import com.gdavidpb.test.domain.usecase.errors.DownloadTrackError
import com.gdavidpb.test.domain.usecase.errors.LookupTracksError
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.utils.extensions.LiveResult
import com.gdavidpb.test.utils.extensions.execute

class AlbumViewModel(
    private val lookupTracksUseCase: LookupTracksUseCase,
    private val downloadTrackUseCase: DownloadTrackUseCase
) : ViewModel() {
    val tracks = LiveResult<List<Track>, LookupTracksError>()
    val download = LiveResult<DownloadTrackResponse, DownloadTrackError>()

    fun lookupTracks(albumId: Long) = execute(
        useCase = lookupTracksUseCase,
        liveData = tracks,
        params = LookupTracksRequest(albumId = albumId)
    )

    fun downloadTrack(item: TrackItem) = execute(
        useCase = downloadTrackUseCase,
        liveData = download,
        params = DownloadTrackRequest(track = item)
    )
}