package com.spoelt.coinpulse.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.coinpulse.domain.connectivity.ConnectivityObserver
import com.spoelt.coinpulse.domain.model.Ticker
import com.spoelt.coinpulse.domain.repository.TickerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TickerRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private var tickersJob: Job? = null

    private val _tickers = MutableStateFlow<List<Ticker>?>(null)
    private val tickers = _tickers.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredTickers = combine(
        tickers,
        searchQuery
    ) { tickers, query ->
        if (query.isBlank()) tickers
        else tickers?.filter { ticker ->
            ticker.abbreviatedName?.contains(query, ignoreCase = true) == true
                    || ticker.label?.contains(query, ignoreCase = true) == true
        }
    }

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _hasNetworkConnection = MutableStateFlow<Boolean?>(null)
    val hasNetworkConnection = _hasNetworkConnection.asStateFlow()

    init {
        observeConnectivityChanges()
    }

    private fun observeConnectivityChanges() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                if (status == ConnectivityObserver.Status.AVAILABLE) {
                    if (!isTickerJobRunning()) getTickersPeriodically()
                } else {
                    cancelJob()
                }

                _hasNetworkConnection.value = status == ConnectivityObserver.Status.AVAILABLE
            }
        }
    }

    fun getTickersPeriodically() {
        tickersJob?.cancel()
        tickersJob = viewModelScope.launch {
            while (isActive) {
                val tickers = repository.getTickers()
                _tickers.update { tickers }
                _isLoading.update { false }

                when {
                    tickers?.isNotEmpty() == true -> delay(GET_TICKER_DELAY)
                    tickers.isNullOrEmpty() -> return@launch
                }
            }
        }
    }

    private fun isTickerJobRunning(): Boolean {
        return tickersJob?.isActive == true
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun cancelJob() {
        tickersJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    fun refresh() {
        _isLoading.value = true
        getTickersPeriodically()
    }

    companion object {
        const val GET_TICKER_DELAY = 5000L
    }
}