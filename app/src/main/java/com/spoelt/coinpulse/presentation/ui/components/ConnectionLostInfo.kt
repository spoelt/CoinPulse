package com.spoelt.coinpulse.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.coinpulse.R
import com.spoelt.coinpulse.presentation.ui.theme.CoinPulseTheme

@Composable
fun ConnectionLostInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = R.drawable.ic_no_network_connection),
            contentDescription = stringResource(R.string.no_network_connection_icon_content_desc),
            tint = MaterialTheme.colorScheme.surface
        )

        Column {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = stringResource(R.string.error_network_connection_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.surface
            )
            Text(
                text = stringResource(R.string.error_network_connection_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }

}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ConnectionLostInfoPreview() {
    CoinPulseTheme {
        ConnectionLostInfo()
    }
}