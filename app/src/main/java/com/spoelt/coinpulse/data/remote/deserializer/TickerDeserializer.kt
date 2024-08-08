package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.spoelt.coinpulse.data.remote.model.TickerDto

object TickerDeserializer {
    private const val INDEX_SYMBOL = 0
    private const val INDEX_DAILY_CHANGE = 6
    private const val INDEX_LAST_PRICE = 7
    private const val TAG = "TickerDeserializer"

    /**
     * Deserializes a JSON string into a list of [TickerDto] objects.
     *
     * @param jsonString The JSON string to parse.
     * @return A list of [TickerDto] objects or null if deserialization fails.
     */
    fun deserializeJsonString(jsonString: String): List<TickerDto>? {
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<List<Any>>>() {}.type
            val data: List<List<Any>> = gson.fromJson(jsonString, type)

            if (data.isEmpty()) {
                Log.e(TAG, "JSON structure error: Not a list of lists")
                return null
            }

            data.map { d ->
                    TickerDto(
                        tradingSymbol = d.getOrNull(INDEX_SYMBOL) as? String,
                        dailyChangeRelative = (d.getOrNull(INDEX_DAILY_CHANGE) as? Number)?.toFloat(),
                        lastPrice = (d.getOrNull(INDEX_LAST_PRICE) as? Number)?.toFloat(),
                    )
            }
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JSON syntax error: ${e.message}")
            null
        } catch (e: ClassCastException) {
            Log.e(TAG, "Type casting error: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}")
            null
        }
    }
}