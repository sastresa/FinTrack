package com.fintrack.ui.screen.dashboard

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.ui.component.AppPanel
import com.fintrack.ui.component.CategoryBadge
import com.fintrack.ui.component.EmptyState
import com.fintrack.ui.component.InfoPill
import com.fintrack.ui.component.MetricCard
import com.fintrack.ui.component.ProgressRow
import com.fintrack.ui.component.ScreenHeader
import com.fintrack.ui.component.SectionHeader

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onQuickAdd: () -> Unit,
    onTransactionSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val categoriesById = state.categories.associateBy { it.id }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = "Dashboard",
                subtitle = state.month.toString(),
                eyebrow = "OVERVIEW",
                trailing = {
                    Button(onClick = onQuickAdd) {
                        Text("Quick add")
                    }
                },
            )
        }
        item {
            AppPanel(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    InfoPill("Current month")
                    Text(
                        text = "Balance",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = state.summary.balance.format(state.currencyCode),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text("Income", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(state.summary.income.format(state.currencyCode), fontWeight = FontWeight.Medium)
                        }
                        Column(Modifier.weight(1f)) {
                            Text("Expenses", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(state.summary.expenses.format(state.currencyCode), fontWeight = FontWeight.Medium)
                        }
                        Column(Modifier.weight(1f)) {
                            Text("Savings", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${(state.summary.savingsRate * 100).toInt()}%", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Income", state.summary.income.format(state.currencyCode), modifier = Modifier.weight(1f), accentHeight = 22.dp)
                MetricCard("Expenses", state.summary.expenses.format(state.currencyCode), modifier = Modifier.weight(1f), accentHeight = 22.dp)
            }
        }
        item {
            SectionHeader("Category breakdown")
            if (state.categoryBreakdown.isEmpty()) {
                EmptyState("No expenses for this month yet.")
            }
        }
        items(state.categoryBreakdown, key = { "category-${it.categoryId}" }) { category ->
            ProgressRow(category.categoryName, category.total, category.percentage, currencyCode = state.currencyCode)
        }
        item {
            SectionHeader("Recent transactions")
            if (state.recentTransactions.isEmpty()) {
                EmptyState("Add your first transaction to start tracking.")
            }
        }
        items(state.recentTransactions, key = { "transaction-${it.id}" }) { transaction ->
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
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                    transaction.date.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (transaction.type.name == "INCOME") "IN" else "OUT",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(26.dp),
                        )
                        Text(transaction.amount.format(state.currencyCode), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
