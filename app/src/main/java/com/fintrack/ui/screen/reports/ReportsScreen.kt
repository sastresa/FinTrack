package com.fintrack.ui.screen.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.fintrack.ui.component.EmptyState
import com.fintrack.ui.component.MetricCard
import com.fintrack.ui.component.ProgressRow
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
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Reports", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Button(onClick = onExport) {
                    Text("Export")
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onPreviousMonth, modifier = Modifier.weight(1f)) {
                    Text("Previous")
                }
                OutlinedButton(onClick = onNextMonth, modifier = Modifier.weight(1f)) {
                    Text("Next")
                }
            }
            Text(state.month.toString(), style = MaterialTheme.typography.titleMedium)
        }
        if (report == null) {
            item { EmptyState("No report data available.") }
        } else {
            item {
                MetricCard("Income", report.summary.income.format(), Modifier.fillMaxWidth())
            }
            item {
                MetricCard("Expenses", report.summary.expenses.format(), Modifier.fillMaxWidth())
            }
            item {
                SectionHeader("Expense breakdown")
                if (report.expenseBreakdown.isEmpty()) {
                    EmptyState("No expenses for this month.")
                }
            }
            items(report.expenseBreakdown, key = { it.categoryId }) { summary ->
                ProgressRow(summary.categoryName, summary.total, summary.percentage)
            }
            item {
                SectionHeader("Income vs expense trend")
            }
            items(report.incomeVsExpenseTrend, key = { it.month }) { point ->
                Text("${point.month}: ${point.income.format()} income, ${point.expenses.format()} expenses")
            }
        }
    }
}
