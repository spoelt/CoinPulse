package com.spoelt.coinpulse.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.coinpulse.domain.model.Ticker
import com.spoelt.coinpulse.presentation.ui.theme.CoinPulseTheme
import kotlin.random.Random

@Composable
fun TickersList(
    modifier: Modifier = Modifier,
    tickers: List<Ticker>,
    state: LazyListState
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = state
    ) {
        items(tickers) { ticker ->
            TickerCard(ticker = ticker)
        }
    }
}

@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
private fun TickersListPreview() {
    CoinPulseTheme {
        TickersList(tickers = (1..10).map {
            Ticker(
                symbol = "Symbol $it",
                dailyChangeRelative = Random.nextFloat(),
                lastPrice = Random.nextFloat(),
                abbreviatedName = "COIN$it",
                label = "Superduper Coin $it"
            )
        }, state = rememberLazyListState())
    }
}