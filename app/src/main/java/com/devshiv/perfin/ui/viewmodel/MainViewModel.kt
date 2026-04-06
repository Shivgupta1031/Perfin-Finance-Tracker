package com.devshiv.perfin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.perfin.data.local.dao.AccountDao
import com.devshiv.perfin.data.local.entity.CategoryEntity
import com.devshiv.perfin.data.local.entity.TransactionEntity
import com.devshiv.perfin.data.repository.FinanceRepository
import com.devshiv.perfin.data.repository.TransactionRepository
import com.devshiv.perfin.utils.Constants.TAG
import com.devshiv.perfin.utils.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountDao: AccountDao
) : ViewModel() {

    val categories = transactionRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val transactions = transactionRepository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val income = transactionRepository.getTotalIncome()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    val expense = transactionRepository.getTotalExpense()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    val accounts = accountDao.getAllAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch {
            Log.d(TAG, "Launched: ")
            val existing = transactionRepository
                .getAllCategories()
                .first()
            Log.d(TAG, "Categories : $existing: ")
            if (existing.isEmpty()) {

                transactionRepository.insertCategory(
                    CategoryEntity(name = "food", type = TransactionType.EXPENSE)
                )

                transactionRepository.insertCategory(
                    CategoryEntity(name = "travel", type = TransactionType.EXPENSE)
                )

                transactionRepository.insertCategory(
                    CategoryEntity(name = "salary", type = TransactionType.INCOME)
                )
            }
        }
    }

    fun addTransaction(txn: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.addTransaction(txn)
        }
    }

    fun updateTransaction(txn: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(txn) // reverse old
            transactionRepository.addTransaction(txn)    // apply new
        }
    }

    fun deleteTransaction(txn: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(txn)
        }
    }
}