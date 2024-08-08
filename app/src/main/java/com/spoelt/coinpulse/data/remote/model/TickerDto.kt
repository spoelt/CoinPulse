package com.spoelt.coinpulse.data.remote.model

/**
 * A DTO representing the details of a crypto currency ticker.
 *
 * @property tradingSymbol The symbol representing the trading pair (e.g., "tBTCUSD" for Bitcoin/USD).
 * @property dailyChangeRelative The relative change in percentage compared to the previous day's closing price.
 * @property lastPrice The most recent transaction price.
 */
data class TickerDto(
    val tradingSymbol: String?,
    val dailyChangeRelative: Float?,
    val lastPrice: Float?,
)