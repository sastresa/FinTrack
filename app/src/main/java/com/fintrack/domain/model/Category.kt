package com.fintrack.domain.model

data class Category(
    val id: Long,
    val name: String,
    val iconName: String,
    val colorToken: String,
    val type: CategoryType,
) {
    fun supports(transactionType: TransactionType): Boolean = when (type) {
        CategoryType.BOTH -> true
        CategoryType.INCOME -> transactionType == TransactionType.INCOME
        CategoryType.EXPENSE -> transactionType == TransactionType.EXPENSE
    }
}
