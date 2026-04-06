package com.devshiv.perfin.di

import com.devshiv.perfin.data.local.dao.*
import com.devshiv.perfin.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideTransactionRepository(
        transactionDao: TransactionDao,
        accountDao: AccountDao,
        categoryDao: CategoryDao
    ): TransactionRepository {
        return TransactionRepository(transactionDao,accountDao, categoryDao)
    }
}