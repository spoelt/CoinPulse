package com.spoelt.coinpulse.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spoelt.coinpulse.domain.model.Ticker
import kotlinx.coroutines.launch

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    tickers: List<Ticker>?,
    onRefresh: () -> Unit,
    hasNetworkConnection: Boolean?
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showLostConnectionInfo = remember(hasNetworkConnection) {
        hasNetworkConnection == false
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            AnimatedVisibility(visible = showLostConnectionInfo) {
                ConnectionLostInfo()
            }

            // hide search bar if error occurred
            if (tickers != null) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    query = searchQuery,
                    onQueryChanged = { newQuery ->
                        if (newQuery != searchQuery) {
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        }
                        onQueryChanged(newQuery)
                    }
                )
            }

            HorizontalDivider(
                color = DividerDefaults.color.copy(alpha = 0.6f)
            )

            when {
                // signals an exception was thrown at some point
                tickers == null -> ErrorScreen(onRefresh)

                // request was successful but no data was returned
                tickers.isEmpty() -> EmptyState(searchQuery, onRefresh)

                // request was successful and data was returned
                else -> TickersList(
                    tickers = tickers,
                    state = listState
                )
            }
        }
    }
}