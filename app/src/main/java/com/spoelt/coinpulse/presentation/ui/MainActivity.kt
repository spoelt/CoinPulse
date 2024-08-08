package com.spoelt.coinpulse.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spoelt.coinpulse.presentation.ui.components.LoadingScreen
import com.spoelt.coinpulse.presentation.ui.components.MainContent
import com.spoelt.coinpulse.presentation.ui.theme.CoinPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CoinPulseTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val filteredTickers by viewModel.filteredTickers.collectAsStateWithLifecycle(
        initialValue = null
    )
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val hasNetworkConnection by viewModel.hasNetworkConnection.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.getTickersPeriodically()
                Lifecycle.Event.ON_STOP -> viewModel.cancelJob()
                else -> {}
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        MainContent(
            searchQuery = searchQuery,
            onQueryChanged = { newQuery -> viewModel.updateSearchQuery(newQuery) },
            tickers = filteredTickers,
            onRefresh = viewModel::refresh,
            hasNetworkConnection = hasNetworkConnection
        )
    }
}