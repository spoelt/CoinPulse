package com.spoelt.coinpulse.domain.repository

import com.spoelt.coinpulse.domain.model.Ticker

interface TickerRepository {
    suspend fun getTickers(): List<Ticker>?
}