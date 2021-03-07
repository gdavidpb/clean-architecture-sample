package com.gdavidpb.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gdavidpb.test.domain.model.Album
import com.gdavidpb.test.domain.model.request.LookupAlbumsRequest
import com.gdavidpb.test.domain.usecase.LookupAlbumsUseCase
import com.gdavidpb.test.domain.usecase.errors.LookupAlbumsError
import com.gdavidpb.test.utils.extensions.LiveResult
import com.gdavidpb.test.utils.extensions.execute

open class ArtistViewModel(
    private val lookupAlbumsUseCase: LookupAlbumsUseCase
) : ViewModel() {
    val albums = LiveResult<List<Album>, LookupAlbumsError>()

    fun lookupAlbums(artistId: Long) = execute(
        useCase = lookupAlbumsUseCase,
        liveData = albums,
        params = LookupAlbumsRequest(artistId = artistId)
    )
}