package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.spoelt.coinpulse.data.remote.deserializer.DeserializerUtils.asStringOrNull
import com.spoelt.coinpulse.data.remote.deserializer.DeserializerUtils.getOrNull

object CoinNameDeserializer {
    private const val INDEX_SYMBOL = 0
    private const val INDEX_LABEL = 1
    private const val TAG = "CoinNameDeserializer"

    /**
     * Deserializes a JSON array into a map consisting of a String-String type. The keys
     * represent the abbreviated name of a cryptocurrency (f.ex. BTC) while the values represent
     * the display name of a cryptocurrency (f.ex. Bitcoin).
     *
     * @param jsonArray The JSON array to parse.
     * @param abbreviatedNames A list of cryptocurrencies that should be filtered for. If list is
     * empty, all cryptocurrencies should be considered.
     * @return A map consisting of the abbreviated name of a cryptocurrency and its display name or
     * null if deserialization fails.
     */
    fun deserializeJsonArray(
        jsonArray: JsonArray?,
        abbreviatedNames: List<String>
    ): Map<String, String>? {
        if (jsonArray == null || jsonArray.none { it.isJsonArray }) return null

        val mappedCoinInfos = jsonArray.flatMap { element ->
            element.asJsonArray.map { innerElement ->
                mapJsonElement(innerElement, abbreviatedNames)
            }
        }

        return if (mappedCoinInfos.all { it == null }) {
            null
        } else {
            val mapped = mutableMapOf<String, String>()
            mappedCoinInfos.filterNotNull().forEach { info ->
                mapped[info.first] = info.second
            }
            mapped
        }
    }

    private fun mapJsonElement(
        element: JsonElement,
        abbreviatedNames: List<String>,
    ): Pair<String, String>? {
        return try {
            val array = element.asJsonArray

            val symbol = array.getOrNull(INDEX_SYMBOL)?.asStringOrNull()
            if (abbreviatedNames.isNotEmpty() && !abbreviatedNames.contains(symbol)) {
                return null
            }

            val label = array.getOrNull(INDEX_LABEL)?.asStringOrNull()

            if (symbol != null && label != null) {
                Pair(symbol, label)
            } else {
                Log.e(TAG, "Invalid or missing data for element: $element")
                null
            }
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
}