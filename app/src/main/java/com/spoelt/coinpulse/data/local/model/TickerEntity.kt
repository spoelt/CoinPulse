package com.spoelt.coinpulse.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a ticker entity in the local database.
 *
 * This data class is used by Room to map to a table named "tickers". Each instance of this class
 * represents a row in the "tickers" table and holds information about a particular ticker symbol.
 *
 * @param symbol The symbol for the ticker (e.g., "BTC", "ETH"). May be null if not applicable.
 * @param dailyChangeRelative The relative daily change of the ticker's price as a percentage.
 * This value may be null if not available.
 * @param lastPrice The last recorded price of the ticker. This value may be null if not available.
 * @param abbreviatedName A unique identifier for the ticker, used as the primary key for this table.
 * @param label A human-readable label for the ticker. May be null if not applicable.
 * @param lastUpdated The timestamp (in milliseconds) when this ticker data was last updated.
 */
@Entity(tableName = "tickers")
data class TickerEntity(
    val symbol: String?,
    @ColumnInfo(name = "daily_change_relative") val dailyChangeRelative: Float?,
    @ColumnInfo(name = "last_price") val lastPrice: Float?,
    @PrimaryKey @ColumnInfo(name = "abbreviated_name") val abbreviatedName: String,
    val label: String?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long
)