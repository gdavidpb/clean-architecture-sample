package com.gdavidpb.test.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gdavidpb.test.data.model.database.AlbumEntity
import com.gdavidpb.test.data.model.database.ArtistEntity
import com.gdavidpb.test.data.model.database.TrackEntity
import com.gdavidpb.test.data.source.local.dao.AlbumDao
import com.gdavidpb.test.data.source.local.dao.ArtistDao
import com.gdavidpb.test.data.source.local.dao.TrackDao
import com.gdavidpb.test.data.model.database.DatabaseModel

@Database(
    entities = [
        ArtistEntity::class,
        AlbumEntity::class,
        TrackEntity::class
    ],
    version = DatabaseModel.VERSION,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract val artists: ArtistDao
    abstract val albums: AlbumDao
    abstract val tracks: TrackDao
}