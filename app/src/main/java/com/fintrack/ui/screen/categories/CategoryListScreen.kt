package com.fintrack.ui.screen.categories

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType
import com.fintrack.ui.component.AppPanel
import com.fintrack.ui.component.CategoryBadge
import com.fintrack.ui.component.CategoryInlineLabel
import com.fintrack.ui.component.ScreenHeader
import com.fintrack.ui.component.categoryIconPresets
import com.fintrack.ui.component.categoryTone

private val colorTokens = listOf("mint", "coral", "blue", "violet", "green", "amber", "slate")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryListScreen(
    state: CategoryListUiState,
    onNameChanged: (String) -> Unit,
    onIconSelected: (String) -> Unit,
    onColorSelected: (String) -> Unit,
    onTypeSelected: (CategoryType) -> Unit,
    onEdit: (Category) -> Unit,
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
            title = "Categories",
            subtitle = "Organize income and expenses with a stable taxonomy.",
            eyebrow = "TAXONOMY",
        )
        AppPanel(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = if (state.editingId == null) "Create category" else "Edit category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                CategoryBadge(
                    iconName = state.iconNameInput,
                    colorToken = state.colorTokenInput,
                    label = state.nameInput.ifBlank { "Preview" },
                    emphasize = true,
                )
                OutlinedTextField(
                    value = state.nameInput,
                    onValueChange = onNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Category name") },
                    singleLine = true,
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Icon",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        categoryIconPresets().forEach { preset ->
                            FilterChip(
                                selected = state.iconNameInput == preset.iconName,
                                onClick = { onIconSelected(preset.iconName) },
                                label = {
                                    CategoryInlineLabel(
                                        iconName = preset.iconName,
                                        colorToken = state.colorTokenInput,
                                        label = preset.label,
                                    )
                                },
                            )
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        colorTokens.forEach { token ->
                            val tone = categoryTone(token)
                            FilterChip(
                                selected = state.colorTokenInput == token,
                                onClick = { onColorSelected(token) },
                                label = {
                                    Text(
                                        text = token.replaceFirstChar { it.uppercase() },
                                        color = if (state.colorTokenInput == token) tone.content else MaterialTheme.colorScheme.onSurface,
                                    )
                                },
                            )
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CategoryType.entries.forEach { type ->
                        FilterChip(
                            selected = state.typeInput == type,
                            onClick = { onTypeSelected(type) },
                            label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        )
                    }
                }
                state.errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                    Text(if (state.editingId == null) "Save category" else "Update category")
                }
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.categories, key = { it.id }) { category ->
                AppPanel(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            CategoryBadge(category = category, emphasize = true)
                            Text(
                                category.type.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { onEdit(category) }) {
                                Text("Edit")
                            }
                            Button(onClick = { onDelete(category.id) }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
