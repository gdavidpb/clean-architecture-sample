package com.gdavidpb.test

import com.gdavidpb.test.data.source.remote.iTunesSearchApi
import com.gdavidpb.test.utils.await
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

    private val artistSearchQuery = "jack"
    private val artistIdQuery = 909253L
    private val albumIdQuery = 879716730L

    private val api: iTunesSearchApi by inject()

    @Before
    fun `start koin`() {
        startKoin(listOf(testModule))
    }

    @Test
    fun `should get artists from iTunes api`() {
        val result = runBlocking {
            api.searchArtists(terms = artistSearchQuery).await()
        }

        assertNotNull(result); result ?: return

        assertTrue(result.resultCount > 0)
    }

    @Test
    fun `should get the artist's albums from iTunes api`() {
        val result = runBlocking {
            api.lookupAlbums(artistId = artistIdQuery).await()
        }

        assertNotNull(result); result ?: return

        assertTrue(result.resultCount > 0)
    }

    @Test
    fun `should get the album's songs from iTunes api`() {
        val result = runBlocking {
            api.lookupTracks(albumId = albumIdQuery).await()
        }

        assertNotNull(result); result ?: return

        assertTrue(result.resultCount > 0)
    }

    @After
    fun `stop koin`() {
        stopKoin()
    }
}
