package com.spoelt.coinpulse.data.remote.api

import android.util.Log
import com.spoelt.coinpulse.data.remote.api.HttpRoutes.CURRENCY_LABELS
import com.spoelt.coinpulse.data.remote.api.HttpRoutes.TICKERS
import com.spoelt.coinpulse.data.remote.deserializer.CoinNameDeserializer
import com.spoelt.coinpulse.data.remote.deserializer.TickerDeserializer
import com.spoelt.coinpulse.data.remote.model.TickerDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException

class CoinServiceImpl(
    private val client: HttpClient
) : CoinService {
    override suspend fun fetchTickers(symbols: String): Result<List<TickerDto>?> {
        return safeApiCall {
            val response = client.get {
                url(TICKERS)
                parameter(SYMBOLS_KEY, symbols)
            }
            TickerDeserializer.deserializeJsonArray(response.body())
        }
    }

    override suspend fun fetchCoinDisplayNames(abbreviatedNames: List<String>): Result<Map<String, String>?> {
        return safeApiCall {
            val response = client.get(CURRENCY_LABELS)
            CoinNameDeserializer.deserializeJsonArray(response.body(), abbreviatedNames)
        }
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            val result = apiCall()
            Result.success(result)
        } catch (e: RedirectResponseException) {
            Log.e(TAG, "Redirecting exception: ${e.response.status.description}")
            Result.failure(e)
        } catch (e: ClientRequestException) {
            Log.e(TAG, "Client request exception: ${e.response.status.description}")
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Log.e(TAG, "Server error exception: ${e.response.status.description}")
            Result.failure(e)
        } catch (e: IOException) {
            Log.e(TAG, "IOException: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}")
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "CoinServiceImpl"
        const val SYMBOLS_KEY = "symbols"
    }
}