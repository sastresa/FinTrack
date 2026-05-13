package com.fintrack.ui.screen.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.domain.model.TransactionType
import com.fintrack.ui.component.AppPanel
import com.fintrack.ui.component.CategoryBadge
import com.fintrack.ui.component.EmptyState
import com.fintrack.ui.component.InfoPill
import com.fintrack.ui.component.ScreenHeader

@Composable
fun TransactionListScreen(
    state: TransactionListUiState,
    onSearchQueryChanged: (String) -> Unit,
    onTypeFilterChanged: (TransactionType?) -> Unit,
    onTransactionSelected: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val categoriesById = state.categories.associateBy { it.id }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenHeader(
            title = "Transactions",
            subtitle = if (state.transactions.isEmpty()) "No matching activity" else "${state.transactions.size} visible",
            eyebrow = "ACTIVITY",
        )
        AppPanel(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                InfoPill("Search and refine")
                OutlinedTextField(
                    value = state.filter.query,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search transactions") },
                    singleLine = true,
                )
                Row(
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
            }
        }
        if (state.transactions.isEmpty()) {
            EmptyState("No transactions match the current filters.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                state.groupedTransactions.forEach { (date, transactions) ->
                    item(key = "date-$date") {
                        Text(
                            date.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    items(transactions, key = { it.id }) { transaction ->
                        val category = categoriesById[transaction.categoryId]
                        AppPanel(
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
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        category?.let {
                                            CategoryBadge(
                                                iconName = it.iconName,
                                                colorToken = it.colorToken,
                                                label = it.name,
                                            )
                                        }
                                        Text(
                                            transaction.amount.format(state.currencyCode),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text(
                                        text = if (transaction.type == TransactionType.INCOME) "IN" else "OUT",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.width(26.dp),
                                    )
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
}
