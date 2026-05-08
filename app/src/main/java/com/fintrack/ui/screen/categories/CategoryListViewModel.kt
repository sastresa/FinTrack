package com.fintrack.ui.screen.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType
import com.fintrack.domain.usecase.FinanceUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val useCases: FinanceUseCases,
) : ViewModel() {
    private val form = MutableStateFlow(CategoryListUiState(isLoading = false))

    val uiState = combine(useCases.getCategories(), form) { categories, formState ->
        formState.copy(isLoading = false, categories = categories)
    }
        .catch { emit(CategoryListUiState(isLoading = false, errorMessage = it.message)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CategoryListUiState())

    fun onNameChanged(value: String) = form.update { it.copy(nameInput = value) }
    fun onIconSelected(value: String) = form.update { it.copy(iconNameInput = value) }
    fun onColorSelected(value: String) = form.update { it.copy(colorTokenInput = value) }
    fun onTypeSelected(value: CategoryType) = form.update { it.copy(typeInput = value) }

    fun onEditClicked(category: Category) {
        form.update {
            it.copy(
                editingId = category.id,
                nameInput = category.name,
                iconNameInput = category.iconName,
                colorTokenInput = category.colorToken,
                typeInput = category.type,
            )
        }
    }

    fun onSaveClicked() {
        val state = form.value
        if (state.nameInput.isBlank()) {
            form.update { it.copy(errorMessage = "Category name is required") }
            return
        }
        viewModelScope.launch {
            useCases.addCategory(
                Category(
                    id = state.editingId ?: 0,
                    name = state.nameInput.trim(),
                    iconName = state.iconNameInput,
                    colorToken = state.colorTokenInput,
                    type = state.typeInput,
                ),
            )
            form.value = CategoryListUiState(isLoading = false)
        }
    }

    fun onDeleteClicked(id: Long) {
        viewModelScope.launch {
            useCases.deleteCategory(id)
        }
    }
}
