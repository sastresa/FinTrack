package com.fintrack.ui.screen.settings

data class SettingsUiState(
    val isLoading: Boolean = true,
    val currencyCode: String = "USD",
    val darkModeEnabled: Boolean = false,
    val statusMessage: String? = null,
    val errorMessage: String? = null,
)
