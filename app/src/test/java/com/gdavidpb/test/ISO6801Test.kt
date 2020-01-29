package com.gdavidpb.test

import com.gdavidpb.test.utils.extensions.parseISO8601Date
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.koin.test.KoinTest

class ISO6801Test : KoinTest {
    @Test
    fun `should parse ISO8601 date`() {
        val dateString = "2008-02-01T08:00:00Z"

        val date = dateString.parseISO8601Date()

        assertNotNull(date)
    }
}
