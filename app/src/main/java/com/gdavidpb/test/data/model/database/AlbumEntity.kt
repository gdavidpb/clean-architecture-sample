package com.gdavidpb.test.data.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DatabaseModel.TABLE_ALBUMS)
data class AlbumEntity(
    @PrimaryKey val collectionId: Long,
    val artistId: Long,
    val artistName: String,
    val collectionName: String,
    val collectionCensoredName: String,
    val artistViewUrl: String,
    val collectionViewUrl: String,
    val artworkUrl30: String?,
    val artworkUrl60: String?,
    val artworkUrl100: String?,
    val collectionPrice: Double,
    val isExplicit: Int,
    val trackCount: Int,
    val copyright: String,
    val country: String,
    val currency: String,
    val releaseDate: Long,
    val primaryGenreName: String
)