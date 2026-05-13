package com.fintrack.ui.screen.reports

import com.fintrack.domain.model.ReportData
import java.time.YearMonth

data class ReportsUiState(
    val isLoading: Boolean = true,
    val month: YearMonth = YearMonth.now(),
    val reportData: ReportData? = null,
    val currencyCode: String = "USD",
    val exportMessage: String? = null,
    val errorMessage: String? = null,
)
