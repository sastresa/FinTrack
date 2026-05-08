package com.fintrack.ui.screen.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.domain.model.TransactionType
import com.fintrack.ui.component.EmptyState

@Composable
fun TransactionListScreen(
    state: TransactionListUiState,
    onSearchQueryChanged: (String) -> Unit,
    onTypeFilterChanged: (TransactionType?) -> Unit,
    onTransactionSelected: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Transactions", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = state.filter.query,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            label = { Text("Search") },
            singleLine = true,
        )
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = state.filter.type == null,
                onClick = { onTypeFilterChanged(null) },
                label = { Text("All") },
            )
            FilterChip(
                selected = state.filter.type == TransactionType.INCOME,
                onClick = { onTypeFilterChanged(TransactionType.INCOME) },
                label = { Text("Income") },
            )
            FilterChip(
                selected = state.filter.type == TransactionType.EXPENSE,
                onClick = { onTypeFilterChanged(TransactionType.EXPENSE) },
                label = { Text("Expense") },
            )
        }
        if (state.transactions.isEmpty()) {
            EmptyState("No transactions match the current filters.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.groupedTransactions.forEach { (date, transactions) ->
                    item(key = "date-$date") {
                        Text(date.toString(), style = MaterialTheme.typography.labelLarge)
                    }
                    items(transactions, key = { it.id }) { transaction ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTransactionSelected(transaction.id) },
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(transaction.title, fontWeight = FontWeight.Medium)
                                    Text(transaction.amount.format(), style = MaterialTheme.typography.bodyMedium)
                                }
                                Button(onClick = { onDelete(transaction.id) }) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
