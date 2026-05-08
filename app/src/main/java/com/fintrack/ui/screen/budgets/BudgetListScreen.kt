package com.fintrack.ui.screen.budgets

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
import com.fintrack.ui.component.ProgressRow

@Composable
fun BudgetListScreen(
    state: BudgetListUiState,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Budgets", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        if (state.progress.isEmpty()) {
            EmptyState("Create a budget to track monthly spending.")
        } else {
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(state.progress, key = { it.budget.id }) { progress ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(progress.budget.month.toString(), fontWeight = FontWeight.Medium)
                                if (progress.isExceeded) {
                                    Text("Exceeded", color = MaterialTheme.colorScheme.error)
                                }
                            }
                            ProgressRow("Spent", progress.spent, progress.percentage)
                            Text("Remaining ${progress.remaining.format()}")
                            Button(onClick = { onDelete(progress.budget.id) }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
