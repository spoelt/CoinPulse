package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CoinNameDeserializerTest {

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
    fun `deserializeJsonString returns map when JSON is valid`() {
        val jsonString = """
            [
                [
                    ["BTC", "Bitcoin"],
                    ["ETH", "Ethereum"]
                ]
            ]
        """
        val expectedMap = mapOf("BTC" to "Bitcoin", "ETH" to "Ethereum")

        val result = CoinNameDeserializer.deserializeJsonString(jsonString)

        assertEquals(expectedMap, result)
    }

    @Test
    fun `deserializeJsonString returns null when JSON is invalid`() {
        val jsonString = """
            [invalid_json
        """

        val result = CoinNameDeserializer.deserializeJsonString(jsonString)

        assertNull(result)
    }

    @Test
    fun `deserializeJsonString returns null when JSON structure is invalid`() {
        val jsonString = """
            [
                {
                    "BTC": "Bitcoin",
                    "ETH": "Ethereum"
                }
            ]
        """

        val result = CoinNameDeserializer.deserializeJsonString(jsonString)

        assertNull(result)
    }

    @Test
    fun `deserializeJsonString returns null when some entries are missing fields`() {
        val jsonString = """
            [
                [
                    ["BTC", "Bitcoin"],
                    ["ETH"]  
                ]
            ]
        """

        val result = CoinNameDeserializer.deserializeJsonString(jsonString)

        val expectedMap = mapOf("BTC" to "Bitcoin")
        assertEquals(expectedMap, result)
    }

    @Test
    fun `deserializeJsonString handles JSON with nested empty arrays`() {
        val jsonString = """
            [
                [],
                [
                    ["BTC", "Bitcoin"]
                ]
            ]
        """
        val expectedMap = mapOf("BTC" to "Bitcoin")

        val result = CoinNameDeserializer.deserializeJsonString(jsonString)

        assertEquals(expectedMap, result)
    }

    @Test
    fun `deserializeJsonString returns null on unexpected errors`() {
        val jsonString = """
            [
                [
                    [123, "ValidLabel"],  // Symbol is not a string
                    ["ETH", "Ethereum"]
                ]
            ]
        """

        val result = CoinNameDeserializer.deserializeJsonString(jsonString)

        val expectedMap = mapOf("ETH" to "Ethereum")
        assertEquals(expectedMap, result)
    }
}