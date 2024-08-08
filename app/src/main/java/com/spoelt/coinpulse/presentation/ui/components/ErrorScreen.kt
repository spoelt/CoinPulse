package com.spoelt.coinpulse.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.coinpulse.R
import com.spoelt.coinpulse.presentation.ui.theme.CoinPulseTheme

@Composable
fun ErrorScreen(
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.fillMaxWidth(0.8f),
            painter = painterResource(id = R.drawable.error_image),
            contentDescription = stringResource(id = R.string.error_image_content_desc),
            contentScale = ContentScale.Fit
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.error_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        ElevatedButton(
            modifier = Modifier.padding(top = 18.dp),
            onClick = onRefresh
        ) {
            Text(text = stringResource(id = R.string.error_retry_action))
        }

        Spacer(modifier = Modifier.weight(1.5f))
    }
}

@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
private fun ErrorScreenPreview() {
    CoinPulseTheme {
        ErrorScreen(
            onRefresh = {}
        )
    }
}