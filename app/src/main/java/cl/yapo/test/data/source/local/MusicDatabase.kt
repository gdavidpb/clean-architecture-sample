package cl.yapo.test.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.yapo.test.data.model.database.AlbumEntity
import cl.yapo.test.data.model.database.ArtistEntity
import cl.yapo.test.data.model.database.TrackEntity
import cl.yapo.test.data.source.local.dao.AlbumDao
import cl.yapo.test.data.source.local.dao.ArtistDao
import cl.yapo.test.data.source.local.dao.TrackDao
import cl.yapo.test.utils.DATABASE_VERSION

@Database(
    entities = [
        ArtistEntity::class,
        AlbumEntity::class,
        TrackEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract val artists: ArtistDao
    abstract val albums: AlbumDao
    abstract val tracks: TrackDao
}