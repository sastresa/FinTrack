package com.fintrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintrack.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY month DESC, id DESC")
    fun observeBudgets(): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(budget: BudgetEntity): Long

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM budgets")
    suspend fun deleteAll()
}
