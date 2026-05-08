package com.fintrack.ui.screen.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.usecase.FinanceUseCases
import java.time.Clock
import java.time.YearMonth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsViewModel(
    private val useCases: FinanceUseCases,
    clock: Clock = Clock.systemDefaultZone(),
) : ViewModel() {
    private val selectedMonth = MutableStateFlow(YearMonth.now(clock))

    val uiState = selectedMonth
        .flatMapLatest { month ->
            useCases.getReportData(month).map { report ->
                ReportsUiState(isLoading = false, month = month, reportData = report)
            }
        }
        .catch { emit(ReportsUiState(isLoading = false, month = selectedMonth.value, errorMessage = it.message)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ReportsUiState(month = selectedMonth.value))

    fun onMonthSelected(month: YearMonth) {
        selectedMonth.value = month
    }

    fun onExportClicked() {
        viewModelScope.launch {
            runCatching { useCases.exportTransactions() }
                .onSuccess { /* Worker-backed export is wired in the app layer. */ }
        }
    }
}
