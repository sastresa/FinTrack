package com.fintrack.ui.screen.dashboard

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.ui.component.EmptyState
import com.fintrack.ui.component.MetricCard
import com.fintrack.ui.component.ProgressRow
import com.fintrack.ui.component.SectionHeader

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onQuickAdd: () -> Unit,
    onTransactionSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Dashboard", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                    Text(state.month.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Button(onClick = onQuickAdd) {
                    Text("Quick add")
                }
            }
        }
        item {
            MetricCard("Balance", state.summary.balance.format(), Modifier.fillMaxWidth())
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Income", state.summary.income.format(), Modifier.weight(1f))
                MetricCard("Expenses", state.summary.expenses.format(), Modifier.weight(1f))
            }
        }
        item {
            MetricCard("Savings rate", "${(state.summary.savingsRate * 100).toInt()}%", Modifier.fillMaxWidth())
        }
        item {
            SectionHeader("Category breakdown")
            if (state.categoryBreakdown.isEmpty()) {
                EmptyState("No expenses for this month yet.")
            }
        }
        items(state.categoryBreakdown, key = { it.categoryId }) { category ->
            ProgressRow(category.categoryName, category.total, category.percentage)
        }
        item {
            SectionHeader("Recent transactions")
            if (state.recentTransactions.isEmpty()) {
                EmptyState("Add your first transaction to start tracking.")
            }
        }
        items(state.recentTransactions, key = { it.id }) { transaction ->
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
                    Column {
                        Text(transaction.title, fontWeight = FontWeight.Medium)
                        Text(transaction.date.toString(), style = MaterialTheme.typography.bodySmall)
                    }
                    Text(transaction.amount.format(), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
