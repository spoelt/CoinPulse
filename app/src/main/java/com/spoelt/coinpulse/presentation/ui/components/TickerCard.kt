package com.spoelt.coinpulse.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.coinpulse.R
import com.spoelt.coinpulse.domain.model.Ticker
import com.spoelt.coinpulse.presentation.ui.theme.CoinPulseTheme
import com.spoelt.coinpulse.presentation.ui.theme.NegativeChange
import com.spoelt.coinpulse.presentation.ui.theme.PositiveChange
import com.spoelt.coinpulse.presentation.utils.NumberFormatUtils

@Composable
fun TickerCard(
    modifier: Modifier = Modifier,
    ticker: Ticker
) {
    val formattedPrice = remember(ticker.lastPrice) {
        ticker.lastPrice?.let { price ->
            NumberFormatUtils.formatFloatAsCurrency(price)
        }
    }
    val formattedChange = remember(ticker.dailyChangeRelative) {
        ticker.dailyChangeRelative?.let { change ->
            NumberFormatUtils.formatFloatAsPercentage(change)
        }
    }
    val neutralChangeColor = MaterialTheme.colorScheme.onSurface
    val changeColor = remember(ticker.dailyChangeRelative) {
        val change = ticker.dailyChangeRelative ?: 0f
        when {
            change > 0f -> PositiveChange
            change < 0f -> NegativeChange
            else -> neutralChangeColor
        }
    }
    val changeIcon = remember(ticker.dailyChangeRelative) {
        val change = ticker.dailyChangeRelative ?: 0f
        when {
            change > 0f -> Icons.Default.KeyboardArrowUp
            change < 0f -> Icons.Default.KeyboardArrowDown
            else -> null
        }
    }

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                ticker.label?.let { label ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                ticker.abbreviatedName?.let { abbrev ->
                    Text(
                        text = abbrev,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                formattedPrice?.let { price ->
                    Text(
                        text = price,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    changeIcon?.let { icon ->
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = icon,
                            tint = changeColor,
                            contentDescription = stringResource(id = R.string.change_icon_content_desc)
                        )
                    }
                    formattedChange?.let { change ->
                        Text(
                            text = change,
                            style = MaterialTheme.typography.bodyMedium,
                            color = changeColor
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TickerCardHighPricePositiveChangePreview() {
    CoinPulseTheme {
        TickerCard(
            modifier = Modifier.fillMaxWidth(),
            ticker = Ticker(
                symbol = null,
                dailyChangeRelative = 0.10f,
                lastPrice = 57555.2f,
                abbreviatedName = "BTC",
                label = "Bitcoin"
            )
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TickerCardLowPriceNegativeChangePreview() {
    CoinPulseTheme {
        TickerCard(
            modifier = Modifier.fillMaxWidth(),
            ticker = Ticker(
                symbol = null,
                dailyChangeRelative = -0.13123f,
                lastPrice = 0.00001321f,
                abbreviatedName = "SHIB",
                label = "Shiba Inu"
            )
        )
    }
}