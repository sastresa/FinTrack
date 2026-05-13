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
import com.fintrack.ui.component.AppPanel
import com.fintrack.ui.component.ScreenHeader

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onCurrencySelected: (String) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit,
    onExport: () -> Unit,
    onBackup: () -> Unit,
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
        ScreenHeader(
            title = "Settings",
            subtitle = "Preferences, export, and local maintenance.",
            eyebrow = "SYSTEM",
        )
        AppPanel(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Currency", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("USD", "EUR", "GBP").forEach { currency ->
                            FilterChip(
                                selected = state.currencyCode == currency,
                                onClick = { onCurrencySelected(currency) },
                                label = { Text(currency) },
                            )
                        }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Dark mode", fontWeight = FontWeight.Medium)
                        Text(
                            "Use the darker surface palette throughout the app.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(checked = state.darkModeEnabled, onCheckedChange = onDarkModeChanged)
                }
            }
        }
        AppPanel(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("Data actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Button(onClick = onCategories, modifier = Modifier.fillMaxWidth()) {
                    Text("Manage categories")
                }
                Button(onClick = onExport, modifier = Modifier.fillMaxWidth()) {
                    Text("Export CSV")
                }
                Button(onClick = onBackup, modifier = Modifier.fillMaxWidth()) {
                    Text("Backup")
                }
                OutlinedButton(onClick = onClearData, modifier = Modifier.fillMaxWidth()) {
                    Text("Clear local data")
                }
            }
        }
        state.statusMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
