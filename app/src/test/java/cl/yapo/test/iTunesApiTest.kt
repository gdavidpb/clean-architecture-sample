package cl.yapo.test

import cl.yapo.test.data.source.remote.iTunesSearchApi
import cl.yapo.test.utils.await
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest

class iTunesApiTest : KoinTest {

    private val artistSearchQuery = "jack johnson"
    private val artistIdQuery = 909253L
    private val albumIdQuery = 879716730L

    private val api by inject<iTunesSearchApi>()

    @Before
    fun `start koin`() {
        startKoin(listOf(testModule))
    }

    @Test
    fun `should get artists from iTunes api`() {
        val result = runBlocking {
            api.searchArtists(terms = artistSearchQuery).await()
        }

        assertNotNull(result)

        val resultCount = result?.resultCount ?: 0

        assertTrue(resultCount > 0)
    }

    @Test
    fun `should get the artist's albums from iTunes api`() {
        val result = runBlocking {
            api.lookupAlbums(artistId = artistIdQuery).await()
        }

        assertNotNull(result)

        val resultCount = result?.resultCount ?: 0

        assertTrue(resultCount > 0)
    }

    @Test
    fun `should get the album's songs from iTunes api`() {
        val result = runBlocking {
            api.lookupTracks(albumId = albumIdQuery).await()
        }

        assertNotNull(result)

        val resultCount = result?.resultCount ?: 0

        assertTrue(resultCount > 0)
    }

    @After
    fun `stop koin`() {
        stopKoin()
    }
}
