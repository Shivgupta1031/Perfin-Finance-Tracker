package com.devshiv.perfin.data.repository

import com.devshiv.perfin.data.local.dao.AccountDao
import com.devshiv.perfin.data.local.dao.CategoryDao
import com.devshiv.perfin.data.local.dao.TransactionDao
import com.devshiv.perfin.data.local.entity.CategoryEntity
import com.devshiv.perfin.data.local.entity.TransactionEntity
import com.devshiv.perfin.utils.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) {

    suspend fun addTransaction(transaction: TransactionEntity) {

        when (transaction.type) {

            TransactionType.INCOME -> {
                accountDao.increaseBalance(
                    transaction.accountId,
                    transaction.amount
                )
            }

            TransactionType.EXPENSE -> {
                accountDao.decreaseBalance(
                    transaction.accountId,
                    transaction.amount
                )
            }

            TransactionType.TRANSFER -> {
                val toAccountId = transaction.toAccountId
                    ?: throw IllegalArgumentException("Transfer requires destination account")

                accountDao.decreaseBalance(
                    transaction.accountId,
                    transaction.amount
                )

                // Add to destination
                accountDao.increaseBalance(
                    toAccountId,
                    transaction.amount
                )
            }
        }

        transactionDao.insert(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {

        when (transaction.type) {

            TransactionType.INCOME -> {
                accountDao.decreaseBalance(
                    transaction.accountId,
                    transaction.amount
                )
            }

            TransactionType.EXPENSE -> {
                accountDao.increaseBalance(
                    transaction.accountId,
                    transaction.amount
                )
            }

            TransactionType.TRANSFER -> {
                val toAccountId = transaction.toAccountId ?: return

                accountDao.increaseBalance(
                    transaction.accountId,
                    transaction.amount
                )

                accountDao.decreaseBalance(
                    toAccountId,
                    transaction.amount
                )
            }
        }

        transactionDao.delete(transaction)
    }

    fun getAllTransactions() = transactionDao.getAllTransactions()

    fun getTotalIncome(): Flow<Double?> = transactionDao.getTotalIncome()

    fun getTotalExpense(): Flow<Double?> = transactionDao.getTotalExpense()

    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }
}