package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import com.google.gson.JsonParser
import com.spoelt.coinpulse.data.remote.model.TickerDto
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun `deserializeJsonArray parses valid JSON correctly`() {
        val jsonString = """
            [
                ["BTC", null, null, null, null, null, 0.02, 35000],
                ["ETH", null, null, null, null, null, 0.03, 2000]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        val expected = listOf(
            TickerDto(tradingSymbol = "BTC", dailyChangeRelative = 0.02f, lastPrice = 35000f),
            TickerDto(tradingSymbol = "ETH", dailyChangeRelative = 0.03f, lastPrice = 2000f)
        )

        val result = TickerDeserializer.deserializeJsonArray(jsonArray)
        assertEquals(expected, result)
    }

    @Test
    fun `deserializeJsonArray returns null for null input array`() {
        val result = TickerDeserializer.deserializeJsonArray(null)
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray returns null for empty JSON array`() {
        val jsonString = "[]"
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        val result = TickerDeserializer.deserializeJsonArray(jsonArray)
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray returns null for JSON with missing fields`() {
        val jsonString = """
            [
                ["BTC"],
                ["ETH", null, null, null, null, null, 0.01]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        val result = TickerDeserializer.deserializeJsonArray(jsonArray)
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray returns null for JSON with unexpected elements`() {
        val jsonString = """
            [
                "BTC",
                {"key": "value"},
                123
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        val result = TickerDeserializer.deserializeJsonArray(jsonArray)
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray filters values for JSON with malformed structures`() {
        val jsonString = """
            [
                ["BTC", null, null, null, null, null, 0.02, 35000],
                ["ETH", null, null, null, null, null, 0.03]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        val result = TickerDeserializer.deserializeJsonArray(jsonArray)
        val expected = listOf(
            TickerDto(
                tradingSymbol = "BTC",
                dailyChangeRelative = 0.02f,
                lastPrice = 35000f
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun `deserializeJsonArray returns null for JSON with empty nested arrays`() {
        val jsonString = """
            [
                [],
                []
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        val result = TickerDeserializer.deserializeJsonArray(jsonArray)
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray handles extra data gracefully`() {
        val jsonString = """
            [
                ["BTC", null, null, null, null, null, 0.02, 35000, "extra"],
                ["ETH", null, null, null, null, null, 0.03, 2000]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        val expected = listOf(
            TickerDto(tradingSymbol = "BTC", dailyChangeRelative = 0.02f, lastPrice = 35000f),
            TickerDto(tradingSymbol = "ETH", dailyChangeRelative = 0.03f, lastPrice = 2000f)
        )

        val result = TickerDeserializer.deserializeJsonArray(jsonArray)
        assertEquals(expected, result)
    }
}