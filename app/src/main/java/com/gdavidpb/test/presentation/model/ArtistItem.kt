package com.gdavidpb.test.presentation.model

data class ArtistItem(
    val artistId: Long,
    val artistName: CharSequence,
    val primaryGenreName: CharSequence,
    val isLiked: Boolean
)