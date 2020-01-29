package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.request.LookupAlbumsRequest
import com.gdavidpb.test.domain.usecase.LookupAlbumsUseCase
import com.gdavidpb.test.utils.LiveResult
import com.gdavidpb.test.utils.execute

open class ArtistViewModel(
    private val lookupAlbumsUseCase: LookupAlbumsUseCase
) : ViewModel() {
    val albums = LiveResult<List<Album>>()

    fun lookupAlbums(artistId: Long) = execute(
        useCase = lookupAlbumsUseCase,
        liveData = albums,
        params = LookupAlbumsRequest(artistId = artistId)
    )
}