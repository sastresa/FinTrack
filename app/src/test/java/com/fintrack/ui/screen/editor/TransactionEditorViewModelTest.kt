package com.fintrack.ui.screen.editor

import com.fintrack.MainDispatcherRule
import com.fintrack.TestFixtures
import com.fintrack.data.FakeFinanceRepository
import com.fintrack.domain.model.TransactionType
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionEditorViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun saveReportsValidationErrorsBeforeWriting() = runTest {
        val repository = FakeFinanceRepository()
        val viewModel = TransactionEditorViewModel(FinanceUseCases.from(repository), clock())

        viewModel.onSaveClicked()
        advanceUntilIdle()

        assertEquals("Title is required", viewModel.uiState.value.fieldErrors["title"])
        assertTrue(repository.transactionsSnapshot().isEmpty())
    }

    @Test
    fun saveValidTransactionWritesToRepository() = runTest {
        val repository = FakeFinanceRepository()
        repository.seedCategories(TestFixtures.groceries)
        val viewModel = TransactionEditorViewModel(FinanceUseCases.from(repository), clock())

        viewModel.onTitleChanged("Coffee")
        viewModel.onAmountChanged("4.50")
        viewModel.onTypeSelected(TransactionType.EXPENSE)
        viewModel.onCategorySelected(TestFixtures.groceries.id)
        viewModel.onDateSelected(LocalDate.of(2026, 5, 8))
        viewModel.onSaveClicked()
        advanceUntilIdle()

        val saved = repository.transactionsSnapshot().single()
        assertEquals("Coffee", saved.title)
        assertEquals(450, saved.amount.minorUnits)
        assertTrue(viewModel.uiState.value.isSaved)
    }

    private fun clock(): Clock = Clock.fixed(Instant.parse("2026-05-08T10:00:00Z"), ZoneOffset.UTC)
}
