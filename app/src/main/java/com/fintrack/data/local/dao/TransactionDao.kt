package com.fintrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintrack.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, updatedAt DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY date DESC, updatedAt DESC")
    suspend fun getTransactionsSnapshot(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransaction(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transaction: TransactionEntity): Long

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}
