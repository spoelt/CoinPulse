package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import com.google.gson.JsonParser
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
    fun `deserializeJsonArray parses valid JSON with matching abbreviated names correctly`() {
        val jsonString = """
            [
                ["BTC", "Bitcoin"],
                ["ETH", "Ethereum"]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val abbreviatedNames = listOf("BTC", "ETH")

        val expected = mapOf("BTC" to "Bitcoin", "ETH" to "Ethereum")

        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, abbreviatedNames)
        assertEquals(expected, result)
    }

    @Test
    fun `deserializeJsonArray returns empty map for JSON with non-matching abbreviated names`() {
        val jsonString = """
            [
                ["BTC", "Bitcoin"],
                ["ETH", "Ethereum"]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val abbreviatedNames = listOf("LTC", "XRP")

        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, abbreviatedNames)
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray returns null for null input array`() {
        val result = CoinNameDeserializer.deserializeJsonArray(null, listOf("BTC", "ETH"))
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray returns null for empty JSON array`() {
        val jsonArray = JsonParser.parseString("[]").asJsonArray
        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, listOf("BTC", "ETH"))
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray does not consider arrays with partial data`() {
        val jsonString = """
            [
                ["BTC", "Bitcoin"],
                ["ETH"]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, listOf("BTC", "ETH"))
        val expected = mapOf(Pair("BTC", "Bitcoin"))
        assertEquals(expected, result)
    }

    @Test
    fun `deserializeJsonArray returns null for JSON with empty nested arrays`() {
        val jsonString = """
            [
                []
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, listOf("BTC", "ETH"))
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray handles extra data in JSON arrays gracefully`() {
        val jsonString = """
            [
                ["BTC", "Bitcoin", "extra"],
                ["ETH", "Ethereum"]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val abbreviatedNames = listOf("BTC", "ETH")

        val expected = mapOf("BTC" to "Bitcoin", "ETH" to "Ethereum")

        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, abbreviatedNames)
        assertEquals(expected, result)
    }

    @Test
    fun `deserializeJsonArray handles all fields missing gracefully`() {
        val jsonString = """
            [
                [null, null],
                [null, null]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, listOf("BTC", "ETH"))
        assertNull(result)
    }

    @Test
    fun `deserializeJsonArray handles type casting errors gracefully`() {
        val jsonString = """
            [
                ["BTC", 12345],
                ["ETH", "Ethereum"]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val abbreviatedNames = listOf("BTC", "ETH")

        val expected = mapOf("ETH" to "Ethereum")

        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, abbreviatedNames)
        assertEquals(expected, result)
    }

    @Test
    fun `deserializeJsonArray returns empty map when no matching symbols in filter`() {
        val jsonString = """
            [
                ["BTC", "Bitcoin"],
                ["ETH", "Ethereum"]
            ]
        """
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray
        val abbreviatedNames = listOf<String>()

        val result = CoinNameDeserializer.deserializeJsonArray(jsonArray, abbreviatedNames)
        val expected = mapOf(Pair("BTC", "Bitcoin"), Pair("ETH", "Ethereum"))
        assertEquals(expected, result)
    }
}