package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Track
import com.gdavidpb.test.domain.model.request.GetTrackPreviewRequest
import com.gdavidpb.test.domain.model.request.LookupTracksRequest
import com.gdavidpb.test.domain.model.response.GetTrackPreviewResponse
import com.gdavidpb.test.domain.usecase.GetTrackPreviewUseCase
import com.gdavidpb.test.domain.usecase.LookupTracksUseCase
import com.gdavidpb.test.domain.usecase.errors.GetTrackPreviewError
import com.gdavidpb.test.domain.usecase.errors.LookupTracksError
import com.gdavidpb.test.presentation.model.TrackItem
import com.gdavidpb.test.utils.extensions.LiveResult
import com.gdavidpb.test.utils.extensions.execute

class AlbumViewModel(
    private val lookupTracksUseCase: LookupTracksUseCase,
    private val getTrackPreviewUseCase: GetTrackPreviewUseCase
) : ViewModel() {
    val tracks = LiveResult<List<Track>, LookupTracksError>()
    val preview = LiveResult<GetTrackPreviewResponse, GetTrackPreviewError>()

    fun lookupTracks(albumId: Long) = execute(
        useCase = lookupTracksUseCase,
        liveData = tracks,
        params = LookupTracksRequest(albumId = albumId)
    )

    fun getTrackPreview(item: TrackItem) = execute(
        useCase = getTrackPreviewUseCase,
        liveData = preview,
        params = GetTrackPreviewRequest(track = item)
    )
}