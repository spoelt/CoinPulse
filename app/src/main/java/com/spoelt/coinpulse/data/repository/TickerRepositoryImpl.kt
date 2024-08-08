package com.spoelt.coinpulse.data.repository

import com.spoelt.coinpulse.data.local.dao.TickerDao
import com.spoelt.coinpulse.data.local.model.TickerEntity
import com.spoelt.coinpulse.data.remote.api.CoinService
import com.spoelt.coinpulse.domain.model.Ticker
import com.spoelt.coinpulse.domain.model.toDomain
import com.spoelt.coinpulse.domain.model.toDomainList
import com.spoelt.coinpulse.domain.model.toEntityList
import com.spoelt.coinpulse.domain.repository.TickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [TickerRepository] interface.
 *
 * This class is responsible for providing ticker data. It fetches data from a remote API if the
 * cache is empty or outdated and updates the local cache. If the cache is up-to-date, it returns
 * the cached data.
 *
 * @param apiService Service for making API calls.
 * @param tickerDao DAO for accessing and updating the local cache.
 */
class TickerRepositoryImpl @Inject constructor(
    private val apiService: CoinService,
    private val tickerDao: TickerDao
) : TickerRepository {
    override suspend fun getTickers(): List<Ticker>? = coroutineScope {
        val cachedData = tickerDao.getAllTickers()
        val currentTime = System.currentTimeMillis()

        if (cachedData.isEmpty() || isCacheOutdated(cachedData, currentTime)) {
            fetchAndUpdateTickers()
        } else {
            cachedData.toDomainList()
        }
    }

    private suspend fun fetchAndUpdateTickers(): List<Ticker>? = withContext(Dispatchers.IO) {
        val tickersResult = async { apiService.fetchTickers(symbolsList) }.await()
        val coinInfosResult = async { apiService.fetchCoinDisplayNames(coinNameAbbreviations) }.await()

        if (tickersResult.isSuccess && coinInfosResult.isSuccess) {
            val tickers = tickersResult.getOrNull()
            val coinInfos = coinInfosResult.getOrNull()
            val mappedTickers = tickers?.map { ticker ->
                val coinName = coinsMap.entries.find { it.value == ticker.tradingSymbol }?.key
                ticker.toDomain(
                    abbreviatedName = coinName,
                    label = coinInfos?.get(coinName)
                )
            } ?: emptyList()

            tickerDao.upsertTickers(mappedTickers.toEntityList(System.currentTimeMillis()))

            mappedTickers
        } else {
            null
        }
    }

    private fun isCacheOutdated(data: List<TickerEntity>, currentTime: Long): Boolean {
        return currentTime - (data.minOfOrNull { it.lastUpdated }
            ?: Long.MAX_VALUE) > CACHE_EXPIRY_TIME_MS
    }

    companion object {
        private val coinsMap = mapOf(
            "BTC" to "tBTCUSD",
            "ETH" to "tETHUSD",
            "JUP" to "tJUPUSD",
            "AVAX" to "tAVAX:USD",
            "BONK" to "tBONK:USD",
            "BORG" to "tBORG:USD",
            "CRV" to "tCRVUSD",
            "DOGE" to "tDOGE:USD",
            "DYM" to "tDYMUSD",
            "MATIC" to "tMATIC:USD",
            "SAND" to "tSAND:USD",
            "SEI" to "tSEIUSD",
            "SHIB" to "tSHIB:USD",
            "SOL" to "tSOLUSD",
            "UNI" to "tUNIUSD",
            "VELAR" to "tVELAR:USD",
            "WIF" to "tWIFUSD",
            "XLM" to "tXLMUSD",
            "ZRO" to "tZROUSD",
        )
        val coinNameAbbreviations = coinsMap.keys.toList()
        val symbolsList = coinsMap.values.joinToString(",")
        private const val CACHE_EXPIRY_TIME_MS = 5000L
    }
}