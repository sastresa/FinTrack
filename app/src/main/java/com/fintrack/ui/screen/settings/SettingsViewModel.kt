package com.fintrack.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.model.Settings
import com.fintrack.domain.usecase.FinanceUseCases
import com.fintrack.worker.BackgroundWorkScheduler
import com.fintrack.worker.NoOpBackgroundWorkScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val useCases: FinanceUseCases,
    private val backgroundWorkScheduler: BackgroundWorkScheduler = NoOpBackgroundWorkScheduler,
) : ViewModel() {
    private var latestSettings = Settings()
    private val statusMessage = MutableStateFlow<String?>(null)

    val uiState = combine(useCases.getSettings(), statusMessage) { settings, status ->
            latestSettings = settings
            SettingsUiState(
                isLoading = false,
                currencyCode = settings.currencyCode,
                darkModeEnabled = settings.darkModeEnabled,
                statusMessage = status,
            )
        }
        .catch { emit(SettingsUiState(isLoading = false, errorMessage = it.message)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    fun onCurrencySelected(currencyCode: String) {
        updateSettings(latestSettings.copy(currencyCode = currencyCode))
    }

    fun onDarkModeChanged(enabled: Boolean) {
        updateSettings(latestSettings.copy(darkModeEnabled = enabled))
    }

    fun onClearLocalDataClicked() {
        viewModelScope.launch {
            useCases.clearLocalData()
            statusMessage.value = "Local data cleared"
        }
    }

    fun onExportClicked() {
        viewModelScope.launch {
            val csv = useCases.exportTransactions()
            backgroundWorkScheduler.enqueueExport(csv)
            statusMessage.value = "Export scheduled"
        }
    }

    fun onBackupClicked() {
        viewModelScope.launch {
            backgroundWorkScheduler.enqueueBackup()
            statusMessage.value = "Backup scheduled"
        }
    }

    private fun updateSettings(settings: Settings) {
        latestSettings = settings
        viewModelScope.launch {
            useCases.updateSettings(settings)
        }
    }
}
