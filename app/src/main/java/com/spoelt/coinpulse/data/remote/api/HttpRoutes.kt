package com.spoelt.coinpulse.data.remote.api

object HttpRoutes {
    private const val BASE_URL = "https://api-pub.bitfinex.com/v2"
    const val TICKERS = "$BASE_URL/tickers"
    const val CURRENCY_LABELS = "$BASE_URL/conf/pub:map:currency:label"
}