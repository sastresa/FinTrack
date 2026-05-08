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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.domain.model.TransactionType
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditorScreen(
    state: TransactionEditorUiState,
    onTitleChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onTypeSelected: (TransactionType) -> Unit,
    onCategorySelected: (Long?) -> Unit,
    onDateSelected: (LocalDate?) -> Unit,
    onNotesChanged: (String) -> Unit,
    onRecurringChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var categoryExpanded by remember { mutableStateOf(false) }
    val selectedCategory = state.categories.firstOrNull { it.id == state.categoryId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (state.id == null) "New transaction" else "Edit transaction",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )
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
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                isError = state.fieldErrors.containsKey("category"),
                supportingText = { state.fieldErrors["category"]?.let { Text(it) } },
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false },
            ) {
                state.categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category.id)
                            categoryExpanded = false
                        },
                    )
                }
            }
        }
        OutlinedTextField(
            value = state.date?.toString().orEmpty(),
            onValueChange = { value -> onDateSelected(runCatching { LocalDate.parse(value) }.getOrNull()) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Date") },
            placeholder = { Text("YYYY-MM-DD") },
            isError = state.fieldErrors.containsKey("date"),
            supportingText = { state.fieldErrors["date"]?.let { Text(it) } },
            singleLine = true,
        )
        OutlinedTextField(
            value = state.notes,
            onValueChange = onNotesChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Notes") },
            minLines = 3,
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Recurring")
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
