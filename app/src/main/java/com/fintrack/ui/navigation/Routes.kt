package com.fintrack.ui.navigation

object Routes {
    const val DASHBOARD = "dashboard"
    const val TRANSACTIONS = "transactions"
    const val TRANSACTION_EDITOR = "transactionEditor"
    const val TRANSACTION_EDITOR_WITH_ID = "transactionEditor/{id}"
    const val CATEGORIES = "categories"
    const val BUDGETS = "budgets"
    const val REPORTS = "reports"
    const val SETTINGS = "settings"

    fun transactionEditor(id: Long? = null): String = id?.let { "$TRANSACTION_EDITOR/$it" } ?: TRANSACTION_EDITOR
}

data class TopLevelDestination(
    val route: String,
    val label: String,
)

val topLevelDestinations = listOf(
    TopLevelDestination(Routes.DASHBOARD, "Dashboard"),
    TopLevelDestination(Routes.TRANSACTIONS, "Transactions"),
    TopLevelDestination(Routes.BUDGETS, "Budgets"),
    TopLevelDestination(Routes.REPORTS, "Reports"),
    TopLevelDestination(Routes.SETTINGS, "Settings"),
)
