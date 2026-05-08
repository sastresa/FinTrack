package com.fintrack.domain.usecase

import com.fintrack.TestFixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class GenerateCsvUseCaseTest {
    @Test
    fun generateCsvEscapesCommasAndQuotes() {
        val transaction = TestFixtures.transaction(1, title = "Lunch, \"team\"")

        val csv = GenerateCsvUseCase()(
            transactions = listOf(transaction),
            categories = listOf(TestFixtures.groceries),
        )

        assertEquals(
            "id,date,type,title,amount,category,notes,isRecurring\n" +
                "1,2026-05-08,expense,\"Lunch, \"\"team\"\"\",10.00,Groceries,,false",
            csv,
        )
    }
}
