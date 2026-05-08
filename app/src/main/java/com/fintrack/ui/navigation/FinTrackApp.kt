package com.fintrack.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fintrack.di.AppContainer
import com.fintrack.domain.usecase.FinanceUseCases
import com.fintrack.ui.screen.budgets.BudgetListScreen
import com.fintrack.ui.screen.budgets.BudgetListViewModel
import com.fintrack.ui.screen.categories.CategoryListScreen
import com.fintrack.ui.screen.categories.CategoryListViewModel
import com.fintrack.ui.screen.dashboard.DashboardScreen
import com.fintrack.ui.screen.dashboard.DashboardViewModel
import com.fintrack.ui.screen.editor.TransactionEditorScreen
import com.fintrack.ui.screen.editor.TransactionEditorViewModel
import com.fintrack.ui.screen.reports.ReportsScreen
import com.fintrack.ui.screen.reports.ReportsViewModel
import com.fintrack.ui.screen.settings.SettingsScreen
import com.fintrack.ui.screen.settings.SettingsViewModel
import com.fintrack.ui.screen.transactions.TransactionListScreen
import com.fintrack.ui.screen.transactions.TransactionListViewModel

@Composable
fun FinTrackApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    val factory = remember(appContainer) {
        FinTrackViewModelFactory(appContainer.useCases, appContainer.backgroundWorkScheduler)
    }
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                topLevelDestinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text(destination.label.take(1)) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.DASHBOARD,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.DASHBOARD) {
                val viewModel: DashboardViewModel = viewModel(factory = factory)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                DashboardScreen(
                    state = state,
                    onQuickAdd = { navController.navigate(Routes.transactionEditor()) },
                    onTransactionSelected = { navController.navigate(Routes.transactionEditor(it)) },
                )
            }
            composable(Routes.TRANSACTIONS) {
                val viewModel: TransactionListViewModel = viewModel(factory = factory)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                TransactionListScreen(
                    state = state,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onTypeFilterChanged = viewModel::onTypeFilterChanged,
                    onTransactionSelected = { navController.navigate(Routes.transactionEditor(it)) },
                    onDelete = viewModel::onDeleteClicked,
                )
            }
            composable(Routes.TRANSACTION_EDITOR) {
                val viewModel: TransactionEditorViewModel = transactionEditorViewModel(appContainer.useCases, null)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                TransactionEditorScreen(
                    state = state,
                    onTitleChanged = viewModel::onTitleChanged,
                    onAmountChanged = viewModel::onAmountChanged,
                    onTypeSelected = viewModel::onTypeSelected,
                    onCategorySelected = viewModel::onCategorySelected,
                    onDateSelected = viewModel::onDateSelected,
                    onNotesChanged = viewModel::onNotesChanged,
                    onRecurringChanged = viewModel::onRecurringChanged,
                    onSave = viewModel::onSaveClicked,
                    onCancel = { navController.popBackStack() },
                )
            }
            composable(
                route = Routes.TRANSACTION_EDITOR_WITH_ID,
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) { entry ->
                val id = entry.arguments?.getLong("id")
                val viewModel: TransactionEditorViewModel = transactionEditorViewModel(appContainer.useCases, id)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                TransactionEditorScreen(
                    state = state,
                    onTitleChanged = viewModel::onTitleChanged,
                    onAmountChanged = viewModel::onAmountChanged,
                    onTypeSelected = viewModel::onTypeSelected,
                    onCategorySelected = viewModel::onCategorySelected,
                    onDateSelected = viewModel::onDateSelected,
                    onNotesChanged = viewModel::onNotesChanged,
                    onRecurringChanged = viewModel::onRecurringChanged,
                    onSave = viewModel::onSaveClicked,
                    onCancel = { navController.popBackStack() },
                )
            }
            composable(Routes.CATEGORIES) {
                val viewModel: CategoryListViewModel = viewModel(factory = factory)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                CategoryListScreen(
                    state = state,
                    onNameChanged = viewModel::onNameChanged,
                    onTypeSelected = viewModel::onTypeSelected,
                    onSave = viewModel::onSaveClicked,
                    onDelete = viewModel::onDeleteClicked,
                )
            }
            composable(Routes.BUDGETS) {
                val viewModel: BudgetListViewModel = viewModel(factory = factory)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                BudgetListScreen(state = state, onDelete = viewModel::onDeleteClicked)
            }
            composable(Routes.REPORTS) {
                val viewModel: ReportsViewModel = viewModel(factory = factory)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                ReportsScreen(
                    state = state,
                    onPreviousMonth = { viewModel.onMonthSelected(state.month.minusMonths(1)) },
                    onNextMonth = { viewModel.onMonthSelected(state.month.plusMonths(1)) },
                    onExport = viewModel::onExportClicked,
                )
            }
            composable(Routes.SETTINGS) {
                val viewModel: SettingsViewModel = viewModel(factory = factory)
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                SettingsScreen(
                    state = state,
                    onCurrencySelected = viewModel::onCurrencySelected,
                    onDarkModeChanged = viewModel::onDarkModeChanged,
                    onExport = viewModel::onExportClicked,
                    onBackup = viewModel::onBackupClicked,
                    onClearData = viewModel::onClearLocalDataClicked,
                    onCategories = { navController.navigate(Routes.CATEGORIES) },
                )
            }
        }
    }
}

@Composable
private fun transactionEditorViewModel(
    useCases: FinanceUseCases,
    id: Long?,
): TransactionEditorViewModel {
    val factory = remember(id, useCases) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransactionEditorViewModel(useCases = useCases, transactionId = id) as T
            }
        }
    }
    return viewModel(factory = factory, key = "transaction-editor-${id ?: "new"}")
}
