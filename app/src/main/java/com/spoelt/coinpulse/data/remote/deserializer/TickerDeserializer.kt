package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.spoelt.coinpulse.data.remote.deserializer.DeserializerUtils.asStringOrNull
import com.spoelt.coinpulse.data.remote.deserializer.DeserializerUtils.getOrNull
import com.spoelt.coinpulse.data.remote.model.TickerDto

object TickerDeserializer {
    private const val INDEX_SYMBOL = 0
    private const val INDEX_DAILY_CHANGE = 6
    private const val INDEX_LAST_PRICE = 7
    private const val TAG = "TickerDeserializer"

    /**
     * Deserializes a JSON array into a list of [TickerDto] objects.
     *
     * @param jsonArray The JSON array to parse.
     * @return A list of [TickerDto] objects or null if deserialization fails.
     */
    fun deserializeJsonArray(jsonArray: JsonArray?): List<TickerDto>? {
        if (jsonArray == null || jsonArray.none { it.isJsonArray }) return null

        val mappedTickers = jsonArray.map { element ->
            mapJsonElement(element)
        }

        return if (mappedTickers.all { it == null }) {
            null
        } else {
            mappedTickers.filterNotNull()
        }
    }

    private fun mapJsonElement(element: JsonElement): TickerDto? {
        return try {
            val array = element.asJsonArray

            val symbol = array.getOrNull(INDEX_SYMBOL)?.asStringOrNull()
            val change = array.getOrNull(INDEX_DAILY_CHANGE)?.asFloatOrNull()
            val price = array.getOrNull(INDEX_LAST_PRICE)?.asFloatOrNull()

            if (symbol != null && change != null && price != null) {
                TickerDto(
                    tradingSymbol = symbol,
                    dailyChangeRelative = change,
                    lastPrice = price
                )
            } else {
                Log.e(TAG, "Invalid or missing data for element: $element")
                null
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "IllegalStateException", e)
            null
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JSON syntax error for element: $element", e)
            null
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "Index out of bounds for element: $element", e)
            null
        } catch (e: ClassCastException) {
            Log.e(TAG, "Type casting error for element: $element", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error for element: $element", e)
            null
        }
    }

    private fun JsonElement?.asFloatOrNull(): Float? =
        this?.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isNumber }?.asFloat
}