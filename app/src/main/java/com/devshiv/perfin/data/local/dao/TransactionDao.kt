package com.devshiv.perfin.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.devshiv.perfin.data.local.entity.TransactionDetailsHelper
import com.devshiv.perfin.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionDetailsHelper>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'INCOME'")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE'")
    fun getTotalExpense(): Flow<Double?>

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()

    @Query("UPDATE transactions SET accountId = :toAccountId WHERE accountId = :fromAccountId")
    suspend fun moveTransactionsToAccount(
        fromAccountId: Int,
        toAccountId: Int
    )

    @Query("""
    UPDATE transactions 
    SET categoryId = :toCategoryId 
    WHERE categoryId = :fromCategoryId
""")
    suspend fun moveTransactionsToCategory(
        fromCategoryId: Int,
        toCategoryId: Int
    )

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'INCOME' AND accountId = :accountId")
    fun getIncomeByAccount(accountId: Int): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE' AND accountId = :accountId")
    fun getExpenseByAccount(accountId: Int): Flow<Double?>

}