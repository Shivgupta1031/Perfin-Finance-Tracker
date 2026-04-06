package com.devshiv.perfin.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devshiv.perfin.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts")
    fun getAllAccountsOnce(): List<AccountEntity>

    @Query("UPDATE accounts SET amount = amount + :amount WHERE id = :accountId")
    suspend fun increaseBalance(accountId: Int, amount: Double)

    @Query("UPDATE accounts SET amount = amount - :amount WHERE id = :accountId")
    suspend fun decreaseBalance(accountId: Int, amount: Double)

    @Delete
    suspend fun delete(account: AccountEntity)
}