package com.fintrack.ui.screen.settings

import com.fintrack.MainDispatcherRule
import com.fintrack.data.FakeFinanceRepository
import com.fintrack.domain.usecase.FinanceUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun settingsEventsPersistPreferences() = runTest {
        val repository = FakeFinanceRepository()
        val viewModel = SettingsViewModel(FinanceUseCases.from(repository))

        viewModel.onCurrencySelected("EUR")
        viewModel.onDarkModeChanged(true)
        advanceUntilIdle()

        assertEquals("EUR", viewModel.uiState.value.currencyCode)
        assertTrue(viewModel.uiState.value.darkModeEnabled)
    }
}
