package com.spoelt.coinpulse.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.spoelt.coinpulse.data.local.model.TickerEntity

@Dao
interface TickerDao {
    @Upsert
    suspend fun upsertTickers(tickers: List<TickerEntity>)

    @Query("SELECT * FROM tickers")
    suspend fun getAllTickers(): List<TickerEntity>
}