package com.spoelt.coinpulse.domain.model

import com.spoelt.coinpulse.data.local.model.TickerEntity
import com.spoelt.coinpulse.data.remote.model.TickerDto

/**
 * A data class representing the details of a crypto currency ticker.
 *
 * @property symbol The symbol representing the trading pair (e.g., "tBTCUSD" for Bitcoin/USD).
 * @property dailyChangeRelative The relative change in percentage compared to the previous day's
 * closing price.
 * @property lastPrice The most recent transaction price.
 * @property abbreviatedName The abbreviated name of the cryptocurrency (e.g., BTC).
 * @property label The display name of the cryptocurrency (e.g., Bitcoin).
 */
data class Ticker(
    val symbol: String?,
    val dailyChangeRelative: Float?,
    val lastPrice: Float?,
    val abbreviatedName: String?,
    val label: String?,
)

fun TickerDto.toDomain(
    abbreviatedName: String?,
    label: String?
) = Ticker(
    symbol = this.tradingSymbol,
    dailyChangeRelative = this.dailyChangeRelative,
    lastPrice = this.lastPrice,
    abbreviatedName = abbreviatedName,
    label = label
)

fun TickerEntity.toDomain() = Ticker(
    symbol = this.symbol,
    dailyChangeRelative = this.dailyChangeRelative,
    lastPrice = this.lastPrice,
    abbreviatedName = abbreviatedName,
    label = label
)

fun List<TickerEntity>.toDomainList() = map { it.toDomain() }

fun Ticker.toEntity(lastUpdated: Long) = TickerEntity(
    symbol = this.symbol,
    dailyChangeRelative = this.dailyChangeRelative,
    lastPrice = this.lastPrice,
    abbreviatedName = abbreviatedName.orEmpty(),
    label = label,
    lastUpdated = lastUpdated
)

fun List<Ticker>.toEntityList(lastUpdated: Long) = map { it.toEntity(lastUpdated) }