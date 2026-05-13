package com.fintrack.ui.screen.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.model.Transaction
import com.fintrack.domain.model.TransactionInput
import com.fintrack.domain.model.TransactionType
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionEditorViewModel(
    private val useCases: FinanceUseCases,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val transactionId: Long? = null,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionEditorUiState(id = transactionId))
    val uiState: StateFlow<TransactionEditorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCases.getCategories().collect { categories ->
                _uiState.update { state ->
                    val compatibleCategoryId = if (
                        state.categoryId == null ||
                        categories.none { it.id == state.categoryId && it.supports(state.type) }
                    ) {
                        categories.firstOrNull { it.supports(state.type) }?.id
                    } else {
                        state.categoryId
                    }
                    state.copy(categories = categories, categoryId = compatibleCategoryId)
                }
            }
        }
        if (transactionId != null) {
            viewModelScope.launch {
                val transaction = useCases.getTransaction(transactionId)
                if (transaction != null) {
                    _uiState.update {
                        it.copy(
                            id = transaction.id,
                            title = transaction.title,
                            amountText = String.format(Locale.US, "%.2f", transaction.amount.minorUnits / 100.0),
                            type = transaction.type,
                            categoryId = transaction.categoryId,
                            date = transaction.date,
                            notes = transaction.notes.orEmpty(),
                            isRecurring = transaction.isRecurring,
                        )
                    }
                }
            }
        }
    }

    fun onTitleChanged(value: String) = _uiState.update { it.copy(title = value, isSaved = false) }
    fun onAmountChanged(value: String) = _uiState.update { it.copy(amountText = value, isSaved = false) }
    fun onTypeSelected(value: TransactionType) = _uiState.update { state ->
        val categoryId = if (state.categories.any { it.id == state.categoryId && it.supports(value) }) {
            state.categoryId
        } else {
            state.categories.firstOrNull { it.supports(value) }?.id
        }
        state.copy(type = value, categoryId = categoryId, isSaved = false)
    }
    fun onCategorySelected(value: Long?) = _uiState.update { it.copy(categoryId = value, isSaved = false) }
    fun onDateSelected(value: LocalDate?) = _uiState.update {
        it.copy(date = value, isDatePickerVisible = false, isSaved = false)
    }
    fun onNotesChanged(value: String) = _uiState.update { it.copy(notes = value, isSaved = false) }
    fun onRecurringChanged(value: Boolean) = _uiState.update { it.copy(isRecurring = value, isSaved = false) }
    fun onDatePickerClicked() = _uiState.update { it.copy(isDatePickerVisible = true) }
    fun onDatePickerDismissed() = _uiState.update { it.copy(isDatePickerVisible = false) }

    fun onSaveClicked() {
        val state = _uiState.value
        val validation = useCases.validateTransactionInput(
            TransactionInput(
                id = state.id,
                title = state.title,
                amountText = state.amountText,
                type = state.type,
                categoryId = state.categoryId,
                date = state.date,
                notes = state.notes,
                isRecurring = state.isRecurring,
            ),
        )
        if (!validation.isValid || validation.draft == null) {
            _uiState.update { it.copy(fieldErrors = validation.fieldErrors, isSaved = false) }
            return
        }

        viewModelScope.launch {
            val now = Instant.now(clock)
            val existing = validation.draft.id?.let { useCases.getTransaction(it) }
            val transaction = Transaction(
                id = validation.draft.id ?: 0,
                type = validation.draft.type,
                amount = validation.draft.amount,
                title = validation.draft.title,
                categoryId = validation.draft.categoryId,
                date = validation.draft.date,
                notes = validation.draft.notes,
                isRecurring = validation.draft.isRecurring,
                createdAt = existing?.createdAt ?: now,
                updatedAt = now,
            )
            val savedId = if (existing == null) {
                useCases.addTransaction(transaction)
            } else {
                useCases.updateTransaction(transaction)
            }
            _uiState.update { it.copy(id = savedId, fieldErrors = emptyMap(), isSaved = true) }
        }
    }

    fun onCancelClicked() = Unit
}
