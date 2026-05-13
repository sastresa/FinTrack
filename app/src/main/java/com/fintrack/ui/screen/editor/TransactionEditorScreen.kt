package com.fintrack.ui.screen.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.domain.model.TransactionType
import com.fintrack.ui.component.AppPanel
import com.fintrack.ui.component.CategoryBadge
import com.fintrack.ui.component.CategoryGlyph
import com.fintrack.ui.component.ScreenHeader
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditorScreen(
    state: TransactionEditorUiState,
    onTitleChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onTypeSelected: (TransactionType) -> Unit,
    onCategorySelected: (Long?) -> Unit,
    onDateSelected: (LocalDate?) -> Unit,
    onDatePickerClicked: () -> Unit,
    onDatePickerDismissed: () -> Unit,
    onNotesChanged: (String) -> Unit,
    onRecurringChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var categoryExpanded by remember { mutableStateOf(false) }
    val availableCategories = state.categories.filter { it.supports(state.type) }
    val selectedCategory = availableCategories.firstOrNull { it.id == state.categoryId }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.date?.toEpochMillis(),
    )

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onSaved()
        }
    }

    if (state.isDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = onDatePickerDismissed,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis?.toLocalDate())
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDatePickerDismissed) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ScreenHeader(
            title = if (state.id == null) "New transaction" else "Edit transaction",
            subtitle = "Keep entries clean and categorized for monthly reporting.",
        )
        AppPanel(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = onTitleChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    isError = state.fieldErrors.containsKey("title"),
                    supportingText = { state.fieldErrors["title"]?.let { Text(it) } },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.amountText,
                    onValueChange = onAmountChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Amount") },
                    isError = state.fieldErrors.containsKey("amount"),
                    supportingText = { state.fieldErrors["amount"]?.let { Text(it) } },
                    singleLine = true,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = state.type == TransactionType.EXPENSE,
                        onClick = { onTypeSelected(TransactionType.EXPENSE) },
                        label = { Text("Expense") },
                    )
                    FilterChip(
                        selected = state.type == TransactionType.INCOME,
                        onClick = { onTypeSelected(TransactionType.INCOME) },
                        label = { Text("Income") },
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        label = { Text("Category") },
                        leadingIcon = {
                            selectedCategory?.let {
                                CategoryGlyph(
                                    iconName = it.iconName,
                                    colorToken = it.colorToken,
                                )
                            }
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        isError = state.fieldErrors.containsKey("category"),
                        supportingText = { state.fieldErrors["category"]?.let { Text(it) } },
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                    ) {
                        if (availableCategories.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No categories available") },
                                onClick = { categoryExpanded = false },
                            )
                        } else {
                            availableCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        CategoryBadge(
                                            category = category,
                                            emphasize = state.categoryId == category.id,
                                        )
                                    },
                                    onClick = {
                                        onCategorySelected(category.id)
                                        categoryExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }
                OutlinedButton(
                    onClick = onDatePickerClicked,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(state.date?.let { "Date: $it" } ?: "Select date")
                }
                state.fieldErrors["date"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = onNotesChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Notes") },
                    minLines = 3,
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Recurring", fontWeight = FontWeight.Medium)
                        Text(
                            "Use for repeating income or expense entries.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(checked = state.isRecurring, onCheckedChange = onRecurringChanged)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(onClick = onSave, modifier = Modifier.weight(1f)) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

private fun LocalDate.toEpochMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
