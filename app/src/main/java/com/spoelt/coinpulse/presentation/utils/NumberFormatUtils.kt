package com.spoelt.coinpulse.presentation.utils

import android.util.Log
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object NumberFormatUtils {
    fun formatFloatAsPercentage(value: Float, locale: Locale = Locale.getDefault()): String {
        val percentFormat = NumberFormat.getPercentInstance(locale).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
        return percentFormat.format(value)
    }

    fun formatFloatAsCurrency(
        value: Float,
        locale: Locale = Locale.getDefault(),
        currencyCode: String = "USD"
    ): String {
        when {
            value < 0f -> {
                Log.e("NumberFormatUtils", "Cannot format negative value for coin price.")
                return ""
            }
            value == Float.MAX_VALUE -> {
                Log.e("NumberFormatUtils", "Cannot format Float.MAX_VALUE value for coin price.")
                return "\u221E"
            }
        }

        val currencyFormat = NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(currencyCode)
            when {
                value == 0f || value == Float.MIN_VALUE -> {
                    minimumFractionDigits = 2
                    maximumFractionDigits = 2
                }

                value < 0.0001f -> {
                    minimumFractionDigits = 8
                    maximumFractionDigits = 8
                }

                value < 1.0f -> {
                    minimumFractionDigits = 4
                    maximumFractionDigits = 4
                }

                else -> {
                    minimumFractionDigits = 2
                    maximumFractionDigits = 2
                }
            }
        }

        return currencyFormat.format(value)
    }
}