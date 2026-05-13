package com.fintrack.ui.screen.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.ui.component.AppPanel
import com.fintrack.ui.component.EmptyState
import com.fintrack.ui.component.MetricCard
import com.fintrack.ui.component.ProgressRow
import com.fintrack.ui.component.ScreenHeader
import com.fintrack.ui.component.SectionHeader

@Composable
fun ReportsScreen(
    state: ReportsUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onExport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val report = state.reportData
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = "Reports",
                subtitle = "Monthly performance and category breakdown.",
                eyebrow = "ANALYSIS",
                trailing = {
                    Button(onClick = onExport) {
                        Text("Export")
                    }
                },
            )
        }
        item {
            AppPanel(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(onClick = onPreviousMonth, modifier = Modifier.weight(1f)) {
                        Text("Previous")
                    }
                    OutlinedButton(onClick = onNextMonth, modifier = Modifier.weight(1f)) {
                        Text("Next")
                    }
                }
            }
            Text(
                state.month.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (report == null) {
            item { EmptyState("No report data available.") }
        } else {
            item {
                MetricCard("Income", report.summary.income.format(state.currencyCode), modifier = Modifier.fillMaxWidth())
            }
            item {
                MetricCard("Expenses", report.summary.expenses.format(state.currencyCode), modifier = Modifier.fillMaxWidth())
            }
            item {
                SectionHeader("Expense breakdown")
                if (report.expenseBreakdown.isEmpty()) {
                    EmptyState("No expenses for this month.")
                }
            }
            items(report.expenseBreakdown, key = { it.categoryId }) { summary ->
                ProgressRow(summary.categoryName, summary.total, summary.percentage, currencyCode = state.currencyCode)
            }
            item {
                SectionHeader("Income vs expense trend")
            }
            items(report.incomeVsExpenseTrend, key = { it.month }) { point ->
                AppPanel(Modifier.fillMaxWidth()) {
                    Text(
                        text = "${point.month}: ${point.income.format(state.currencyCode)} income, " +
                            "${point.expenses.format(state.currencyCode)} expenses",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
