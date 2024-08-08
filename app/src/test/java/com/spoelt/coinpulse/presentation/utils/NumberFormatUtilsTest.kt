package com.spoelt.coinpulse.presentation.utils

import android.util.Log
import com.spoelt.coinpulse.presentation.utils.NumberFormatUtils.formatFloatAsCurrency
import com.spoelt.coinpulse.presentation.utils.NumberFormatUtils.formatFloatAsPercentage
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

class NumberFormatUtilsTest {
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
    fun formatFloatAsCurrency_highValue() {
        val value = 45000.0f
        val result = formatFloatAsCurrency(value, Locale.GERMAN)
        val expected = "45.000,00 $"
        assertEquals(expected, result)
    }

    @Test
    fun formatFloatAsCurrency_lowValue() {
        val value = 0.1234f
        val result = formatFloatAsCurrency(value, Locale.FRENCH, "EUR")
        val expected = "0,1234 €"
        assertEquals(expected, result)
    }

    @Test
    fun formatFloatAsCurrency_veryLowValue() {
        val value = 0.00001234f
        val result = formatFloatAsCurrency(value, Locale.GERMAN)
        val expected = "0,00001234 $"
        assertEquals(expected, result)
    }

    @Test
    fun formatFloatAsCurrency_zeroValue() {
        val value = 0.0f
        val result = formatFloatAsCurrency(value, Locale.US)
        val expected = "$0.00"

        assertEquals(expected, result)
    }

    @Test
    fun formatFloatAsCurrency_floatMaxValue() {
        val value = Float.MAX_VALUE
        val result = formatFloatAsCurrency(value, Locale.US)
        val expected = "\u221E"

        assertEquals(expected, result)
    }

    @Test
    fun formatFloatAsCurrency_floatMinValue() {
        val value = Float.MIN_VALUE
        val result = formatFloatAsCurrency(value, Locale.GERMAN)
        val expected = "0,00 $"

        assertEquals(expected, result)
    }

    @Test
    fun formatFloatAsPercentage_zeroValue() {
        val value = 0.0f
        val result = formatFloatAsPercentage(value, Locale.US)
        val expected = "0.00%"

        assertEquals(expected, result)
    }

    @Test
    fun formatFloatAsPercentage_mediumValue() {
        val value = 0.1234f
        val result = formatFloatAsPercentage(value, Locale.GERMAN)
        val expected = "12,34 %"

        assertEquals(expected, result)
    }
}