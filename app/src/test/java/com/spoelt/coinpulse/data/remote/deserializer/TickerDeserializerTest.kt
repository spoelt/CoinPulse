package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import com.spoelt.coinpulse.data.remote.model.TickerDto
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TickerDeserializerTest {

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `deserializeJsonString parses valid JSON correctly`() {
        val jsonString = """
            [
                ["BTC", null, null, null, null, null, 0.02, 35000],
                ["ETH", null, null, null, null, null, 0.03, 2000]
            ]
        """
        val expected = listOf(
            TickerDto(tradingSymbol = "BTC", dailyChangeRelative = 0.02f, lastPrice = 35000f),
            TickerDto(tradingSymbol = "ETH", dailyChangeRelative = 0.03f, lastPrice = 2000f)
        )

        val result = TickerDeserializer.deserializeJsonString(jsonString)

        assertEquals(expected, result)
    }

    @Test
    fun `deserializeJsonString returns null for malformed JSON`() {
        val jsonString = """
            [invalid json]
        """

        val result = TickerDeserializer.deserializeJsonString(jsonString)

        assertNull(result)
    }

    @Test
    fun `deserializeJsonString returns null for empty JSON array`() {
        val jsonString = "[]"

        val result = TickerDeserializer.deserializeJsonString(jsonString)

        assertTrue(result.isNullOrEmpty())
    }

    @Test
    fun `deserializeJsonString handles JSON with missing fields gracefully`() {
        // Arrange
        val jsonString = """
            [
                ["BTC"],
                ["ETH", null, null, null, null, null, 0.01]
            ]
        """
        val expected = listOf(
            TickerDto(tradingSymbol = "BTC", dailyChangeRelative = null, lastPrice = null),
            TickerDto(tradingSymbol = "ETH", dailyChangeRelative = 0.01f, lastPrice = null)
        )

        // Act
        val result = TickerDeserializer.deserializeJsonString(jsonString)

        // Assert
        assertEquals(expected, result)
    }
}