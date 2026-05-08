package com.fintrack.ui.screen.editor

import com.fintrack.domain.model.Category
import com.fintrack.domain.model.TransactionType
import java.time.LocalDate

data class TransactionEditorUiState(
    val isLoading: Boolean = false,
    val id: Long? = null,
    val title: String = "",
    val amountText: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long? = null,
    val date: LocalDate? = null,
    val notes: String = "",
    val isRecurring: Boolean = false,
    val categories: List<Category> = emptyList(),
    val fieldErrors: Map<String, String> = emptyMap(),
    val errorMessage: String? = null,
    val isSaved: Boolean = false,
)
