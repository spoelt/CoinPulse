package com.spoelt.coinpulse.data.remote.deserializer

import com.google.gson.JsonArray
import com.google.gson.JsonElement

object DeserializerUtils {
    fun JsonArray.getOrNull(index: Int): JsonElement? =
        if (index in 0 until this.size()) this[index] else null

    fun JsonElement?.asStringOrNull(): String? =
        this?.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isString }?.asString
}