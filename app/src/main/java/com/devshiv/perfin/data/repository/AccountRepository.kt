package com.devshiv.perfin.data.repository

import com.devshiv.perfin.data.local.dao.AccountDao
import com.devshiv.perfin.data.local.dao.TransactionDao
import com.devshiv.perfin.data.local.entity.AccountEntity
import com.devshiv.perfin.utils.AccountType
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) {

    fun getAccounts() = accountDao.getAllAccounts()

    suspend fun addAccount(account: AccountEntity) {
        accountDao.insert(account)
    }

    suspend fun updateAccount(account: AccountEntity) {
        accountDao.insert(account)
    }

    suspend fun deleteAccountSafe(
        account: AccountEntity,
        defaultAccountId: Int
    ) {

        transactionDao.moveTransactionsToAccount(
            fromAccountId = account.id,
            toAccountId = defaultAccountId
        )

        accountDao.delete(account)
    }

    fun getIncomeByAccount(accountId: Int) =
        transactionDao.getIncomeByAccount(accountId)

    fun getExpenseByAccount(accountId: Int) =
        transactionDao.getExpenseByAccount(accountId)

}