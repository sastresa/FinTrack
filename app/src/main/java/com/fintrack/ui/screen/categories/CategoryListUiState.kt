package com.fintrack.ui.screen.categories

import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType

data class CategoryListUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val editingId: Long? = null,
    val nameInput: String = "",
    val iconNameInput: String = "category",
    val colorTokenInput: String = "blue",
    val typeInput: CategoryType = CategoryType.BOTH,
    val errorMessage: String? = null,
)
