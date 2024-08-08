package com.spoelt.coinpulse.data.remote.deserializer

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object CoinNameDeserializer {
    private const val INDEX_SYMBOL = 0
    private const val INDEX_LABEL = 1
    private const val TAG = "CoinNameDeserializer"

    /**
     * Deserializes a JSON string into a map consisting of a String-String type. The keys
     * represent the abbreviated name of a cryptocurrency (f.ex. BTC) while the values represent
     * the display name of a cryptocurrency (f.ex. Bitcoin).
     *
     * @param jsonString The JSON string to parse.
     * @return A map consisting of the abbreviated name of a cryptocurrency and its display name or
     * null if deserialization fails.
     */
    fun deserializeJsonString(jsonString: String): Map<String, String>? {
        return try {
            val coins = mutableMapOf<String, String>()
            val gson = Gson()
            val type = object : TypeToken<List<List<List<Any>>>>() {}.type
            val data: List<List<List<Any>>> = gson.fromJson(jsonString, type)

            if (data.isEmpty()) {
                Log.e(TAG, "empty JSON")
                return null
            }

            data.flatMap { d ->
                d.map {
                    val symbol = it.getOrNull(INDEX_SYMBOL) as? String
                    val label = it.getOrNull(INDEX_LABEL) as? String
                    if (symbol != null && label != null) {
                        coins[symbol] = label
                    } else {
                        return@map
                    }
                }
            }
            coins
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