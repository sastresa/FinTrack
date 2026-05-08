package com.fintrack.domain.model

data class Settings(
    val currencyCode: String = "USD",
    val darkModeEnabled: Boolean = false,
)
