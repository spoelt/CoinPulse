package com.spoelt.coinpulse.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.spoelt.coinpulse.R
import com.spoelt.coinpulse.presentation.ui.theme.CoinPulseTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    placeholder: String = stringResource(id = R.string.search_bar_label)
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier.onPreInterceptKeyBeforeSoftKeyboard { event ->
            if (event.key.nativeKeyCode == android.view.KeyEvent.KEYCODE_BACK) {
                focusManager.clearFocus()
                true
            } else {
                false
            }
        },
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = CircleShape,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon_content_desc),
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarEmptyPreview() {
    CoinPulseTheme {
        SearchBar(
            query = "",
            onQueryChanged = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarFilledPreview() {
    CoinPulseTheme {
        SearchBar(
            query = "btc",
            onQueryChanged = {}
        )
    }
}