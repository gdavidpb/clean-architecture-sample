package com.gdavidpb.test.data.model.api

data class ArtistEntry(
    val artistId: Long,
    val artistName: String,
    val artistLinkUrl: String?,
    val primaryGenreName: String?,
    val primaryGenreId: Long?
)