package com.fintrack.ui.screen.budgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.fintrack.ui.component.AppPanel
import com.fintrack.ui.component.CategoryInlineLabel
import com.fintrack.ui.component.EmptyState
import com.fintrack.ui.component.ProgressRow
import com.fintrack.ui.component.ScreenHeader

@Composable
fun BudgetListScreen(
    state: BudgetListUiState,
    onCategorySelected: (Long?) -> Unit,
    onMonthChanged: (String) -> Unit,
    onLimitAmountChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ScreenHeader(
            title = "Budgets",
            subtitle = "Set monthly limits and monitor category spend.",
            eyebrow = "PLANNING",
        )
        AppPanel(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Add budget", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedCategoryId == null,
                            onClick = { onCategorySelected(null) },
                            label = { Text("Overall") },
                        )
                    }
                    items(state.categories, key = { it.id }) { category ->
                        FilterChip(
                            selected = state.selectedCategoryId == category.id,
                            onClick = { onCategorySelected(category.id) },
                            label = {
                                CategoryInlineLabel(
                                    iconName = category.iconName,
                                    colorToken = category.colorToken,
                                    label = category.name,
                                )
                            },
                        )
                    }
                }
                OutlinedTextField(
                    value = state.monthInput,
                    onValueChange = onMonthChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Month") },
                    placeholder = { Text("YYYY-MM") },
                    isError = state.fieldErrors.containsKey("month"),
                    supportingText = { state.fieldErrors["month"]?.let { Text(it) } },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.limitAmountText,
                    onValueChange = onLimitAmountChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Limit amount") },
                    isError = state.fieldErrors.containsKey("limit"),
                    supportingText = { state.fieldErrors["limit"]?.let { Text(it) } },
                    singleLine = true,
                )
                Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                    Text("Save budget")
                }
            }
        }
        if (state.progress.isEmpty()) {
            EmptyState("Create a budget to track monthly spending.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(state.progress, key = { it.budget.id }) { progress ->
                    AppPanel(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(progress.budget.month.toString(), fontWeight = FontWeight.Medium)
                                if (progress.isExceeded) {
                                    Text("Exceeded", color = MaterialTheme.colorScheme.error)
                                }
                            }
                            ProgressRow("Spent", progress.spent, progress.percentage, currencyCode = state.currencyCode)
                            Text(
                                "Remaining ${progress.remaining.format(state.currencyCode)}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
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
