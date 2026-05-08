package com.fintrack.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.domain.model.Settings
import com.fintrack.domain.usecase.FinanceUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val useCases: FinanceUseCases,
) : ViewModel() {
    private var latestSettings = Settings()

    val uiState = useCases.getSettings()
        .map { settings ->
            latestSettings = settings
            SettingsUiState(
                isLoading = false,
                currencyCode = settings.currencyCode,
                darkModeEnabled = settings.darkModeEnabled,
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
        }
    }

    private fun updateSettings(settings: Settings) {
        latestSettings = settings
        viewModelScope.launch {
            useCases.updateSettings(settings)
        }
    }
}
