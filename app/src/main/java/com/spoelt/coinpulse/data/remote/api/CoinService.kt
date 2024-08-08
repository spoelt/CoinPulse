package com.spoelt.coinpulse.data.remote.api

import com.spoelt.coinpulse.data.remote.model.TickerDto

interface CoinService {
    suspend fun fetchTickers(symbols: String): Result<List<TickerDto>?>

    suspend fun fetchCoinDisplayNames(abbreviatedNames: List<String>): Result<Map<String, String>?>
}