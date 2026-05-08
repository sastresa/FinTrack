package com.fintrack.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onCurrencySelected: (String) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit,
    onClearData: () -> Unit,
    onCategories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Text("Currency", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("USD", "EUR", "GBP").forEach { currency ->
                FilterChip(
                    selected = state.currencyCode == currency,
                    onClick = { onCurrencySelected(currency) },
                    label = { Text(currency) },
                )
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Dark mode")
            Switch(checked = state.darkModeEnabled, onCheckedChange = onDarkModeChanged)
        }
        Button(onClick = onCategories, modifier = Modifier.fillMaxWidth()) {
            Text("Manage categories")
        }
        OutlinedButton(onClick = onClearData, modifier = Modifier.fillMaxWidth()) {
            Text("Clear local data")
        }
    }
}
