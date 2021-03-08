package com.gdavidpb.test.presentation.model

import androidx.annotation.DrawableRes

data class AlbumItem(
    val collectionId: Long,
    val collectionName: CharSequence,
    val artistName: CharSequence,
    val genreAndYear: CharSequence,
    val artworkUrl: String,
    @DrawableRes
    val nameIconResource: Int
)