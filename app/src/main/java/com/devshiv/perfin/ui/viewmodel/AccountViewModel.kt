package com.devshiv.perfin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.perfin.data.local.entity.AccountEntity
import com.devshiv.perfin.data.repository.AccountRepository
import com.devshiv.perfin.utils.AccountType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repo: AccountRepository
) : ViewModel() {

    val accounts = repo.getAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addAccountEntity(account: AccountEntity) {
        viewModelScope.launch {
            repo.addAccount(account)
        }
    }

    fun updateAccount(account: AccountEntity) {
        viewModelScope.launch {
            repo.updateAccount(account)
        }
    }

    fun delete(account: AccountEntity) {
        viewModelScope.launch {

            val default = accounts.value.firstOrNull { it.type == AccountType.DEFAULT }
                ?: return@launch

            if (account.id == default.id) return@launch

            repo.deleteAccountSafe(
                account = account,
                defaultAccountId = default.id
            )
        }
    }

    fun getAccountStats(accountId: Int): Flow<Pair<Double, Double>> {
        return combine(
            repo.getIncomeByAccount(accountId),
            repo.getExpenseByAccount(accountId)
        ) { income, expense ->
            Pair(income ?: 0.0, expense ?: 0.0)
        }
    }
}