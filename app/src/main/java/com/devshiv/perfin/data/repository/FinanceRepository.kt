package com.devshiv.perfin.data.repository

import com.devshiv.perfin.data.local.dao.TransactionDao
import com.devshiv.perfin.data.local.entity.TransactionEntity
import javax.inject.Inject

class FinanceRepository @Inject constructor(
    private val dao: TransactionDao
) {

    fun getAll() = dao.getAllTransactions()

    fun getIncome() = dao.getTotalIncome()

    fun getExpense() = dao.getTotalExpense()

    suspend fun insert(transaction: TransactionEntity) {
        dao.insert(transaction)
    }

    suspend fun delete(transaction: TransactionEntity) {
        dao.delete(transaction)
    }
}