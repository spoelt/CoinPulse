package com.spoelt.coinpulse.data.repository

import com.spoelt.coinpulse.data.local.dao.TickerDao
import com.spoelt.coinpulse.data.local.model.TickerEntity
import com.spoelt.coinpulse.data.remote.api.CoinService
import com.spoelt.coinpulse.domain.model.toDomainList
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TickerRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: TickerRepositoryImpl
    private lateinit var apiService: CoinService
    private lateinit var tickerDao: TickerDao

    private val mockTickerEntities = (1..10).map {
        TickerEntity(
            symbol = "ticker$it",
            dailyChangeRelative = 0f,
            lastPrice = 10f * it,
            abbreviatedName = "tck$it",
            label = "Ticker $it",
            lastUpdated = System.currentTimeMillis()
        )
    }

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        apiService = mockk()
        tickerDao = mockk()
        repository = TickerRepositoryImpl(apiService, tickerDao)
    }

    @Test
    fun `getTickers returns cached data when cache is valid`(): Unit = runTest {
        val cachedTickers = mockTickerEntities
        coEvery { tickerDao.upsertTickers(mockTickerEntities) }
        coEvery { tickerDao.getAllTickers() } returns cachedTickers

        val result = repository.getTickers()

        assertEquals(cachedTickers.toDomainList(), result)
        coVerify(exactly = 0) { apiService.fetchTickers(any()) }
    }

    @Test
    fun `getTickers fetches and updates tickers when cache is outdated`() = runTest {
        val cachedTickers = mockTickerEntities.map {
            it.copy(lastUpdated = System.currentTimeMillis().minus(10_000L))
        }
        coEvery { tickerDao.getAllTickers() } returns cachedTickers
        coEvery { apiService.fetchTickers(any()) } returns Result.success(emptyList())
        coEvery { apiService.fetchCoinDisplayNames(any()) } returns Result.success(emptyMap())
        coEvery { tickerDao.upsertTickers(any()) } just Runs

        val result = repository.getTickers()

        assertTrue(result.isNullOrEmpty())
        coVerify(exactly = 1) { apiService.fetchTickers(any()) }
        coVerify(exactly = 1) { apiService.fetchCoinDisplayNames(any()) }
        coVerify(exactly = 1) { tickerDao.upsertTickers(any()) }
    }

    @Test
    fun `getTickers returns null when API call fails`() = runTest {
        val cachedTickers = mockTickerEntities.map {
            it.copy(lastUpdated = System.currentTimeMillis().minus(10_000L))
        }
        coEvery { tickerDao.getAllTickers() } returns cachedTickers
        coEvery { apiService.fetchTickers(any()) } returns Result.failure(RuntimeException("API Error"))
        coEvery { apiService.fetchCoinDisplayNames(any()) } returns Result.success(emptyMap())
        coEvery { tickerDao.upsertTickers(any()) } just Runs

        val result = repository.getTickers()

        assertNull(result)
        coVerify(exactly = 0) { tickerDao.upsertTickers(any()) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}