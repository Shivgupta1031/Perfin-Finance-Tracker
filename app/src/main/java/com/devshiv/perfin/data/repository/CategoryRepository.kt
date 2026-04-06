package com.devshiv.perfin.data.repository

import com.devshiv.perfin.data.local.dao.AccountDao
import com.devshiv.perfin.data.local.dao.CategoryDao
import com.devshiv.perfin.data.local.dao.TransactionDao
import com.devshiv.perfin.data.local.entity.AccountEntity
import com.devshiv.perfin.data.local.entity.CategoryEntity
import com.devshiv.perfin.utils.AccountType
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) {

    fun getCategories() = categoryDao.getAllCategories()

    suspend fun addCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    suspend fun deleteCategorySafe(category: CategoryEntity) {

        val all = categoryDao.getAllCategoriesOnce()

        val fallback = all.firstOrNull {
            it.name.lowercase() == "other"
        } ?: run {

            val newId = categoryDao.insert(
                CategoryEntity(
                    name = "other",
                    type = category.type
                )
            )

            CategoryEntity(id = newId.toInt(), name = "other", type = category.type)
        }

        transactionDao.moveTransactionsToCategory(
            fromCategoryId = category.id,
            toCategoryId = fallback.id
        )

        categoryDao.delete(category)
    }

    fun getIncomeByAccount(accountId: Int) =
        transactionDao.getIncomeByAccount(accountId)

    fun getExpenseByAccount(accountId: Int) =
        transactionDao.getExpenseByAccount(accountId)

}