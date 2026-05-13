package com.fintrack.data.bootstrap

import com.fintrack.domain.model.Category
import com.fintrack.domain.model.CategoryType

object DefaultCategories {
    val values = listOf(
        Category(0, "Groceries", "shopping_cart", "mint", CategoryType.EXPENSE),
        Category(0, "Dining", "restaurant", "coral", CategoryType.EXPENSE),
        Category(0, "Transport", "directions_car", "blue", CategoryType.EXPENSE),
        Category(0, "Rent", "home", "violet", CategoryType.EXPENSE),
        Category(0, "Salary", "payments", "green", CategoryType.INCOME),
        Category(0, "Freelance", "work", "amber", CategoryType.INCOME),
        Category(0, "Transfer", "swap_horiz", "slate", CategoryType.BOTH),
    )
}
