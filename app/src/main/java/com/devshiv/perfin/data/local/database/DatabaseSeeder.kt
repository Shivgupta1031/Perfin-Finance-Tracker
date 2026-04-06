package com.devshiv.perfin.data.local.database

import android.util.Log
import com.devshiv.perfin.data.local.dao.AccountDao
import com.devshiv.perfin.data.local.dao.CategoryDao
import com.devshiv.perfin.data.local.dao.SettingsDao
import com.devshiv.perfin.data.local.entity.AccountEntity
import com.devshiv.perfin.data.local.entity.CategoryEntity
import com.devshiv.perfin.data.local.entity.SettingsEntity
import com.devshiv.perfin.ui.theme.PrimaryGreen
import com.devshiv.perfin.ui.theme.toHex
import com.devshiv.perfin.utils.AccountType
import com.devshiv.perfin.utils.CategoryIcons
import com.devshiv.perfin.utils.Constants.Currencies
import com.devshiv.perfin.utils.Constants.TAG
import com.devshiv.perfin.utils.TransactionType
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.none
import kotlinx.coroutines.flow.toSet
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val settingsDao: SettingsDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) {

    suspend fun seed() {
        Log.d(TAG, "seeding started")

        seedSettings()
        seedAccounts()
        seedCategories()

        Log.d(TAG, "seeding completed")
    }

    private suspend fun seedSettings() {

        val existing = settingsDao.getSettingsOnce()

        if (existing == null) {
            Log.d(TAG, "inserting default settings")

            settingsDao.insert(
                SettingsEntity(
                    isDarkMode = false,
                    currency = Currencies[0],
                    appLockEnabled = false,
                    onboardingShown = false
                )
            )

        } else {
            Log.d(TAG, "settings already exist: $existing")
        }
    }

    private suspend fun seedCategories() {

        val existing = categoryDao.getAllCategoriesOnce()
            .map { it.name.lowercase() }
            .toSet()

        Log.d(TAG, "existing categories: $existing")

        val defaults = listOf(

            CategoryEntity(
                name = "Food",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.FOOD
            ),
            CategoryEntity(
                name = "Travel",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.TRAVEL
            ),
            CategoryEntity(
                name = "Shopping",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.SHOPPING
            ),
            CategoryEntity(
                name = "Bills",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.BILLS
            ),
            CategoryEntity(
                name = "Health",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.HEALTH
            ),
            CategoryEntity(
                name = "Entertainment",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.ENTERTAINMENT
            ),
            CategoryEntity(
                name = "Education",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.EDUCATION
            ),
            CategoryEntity(
                name = "Transport",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.TRANSPORT
            ),

            // income categories
            CategoryEntity(
                name = "Salary",
                type = TransactionType.INCOME,
                icon = CategoryIcons.SALARY
            ),
            CategoryEntity(
                name = "Business",
                type = TransactionType.INCOME,
                icon = CategoryIcons.BUSINESS
            ),

            CategoryEntity(
                name = "Other",
                type = TransactionType.EXPENSE,
                icon = CategoryIcons.OTHER
            )
        )

        val toInsert = defaults.filter {
            !existing.contains(it.name.lowercase())
        }

        if (toInsert.isNotEmpty()) {
            categoryDao.insertAll(toInsert)
        }
    }

    private suspend fun seedAccounts() {

        val existing = accountDao.getAllAccountsOnce()

        Log.d(TAG, "existing accounts count: ${existing.size}")

        if (existing.none { it.type == AccountType.DEFAULT }) {

            Log.d(TAG, "inserting default account")

            accountDao.insert(
                AccountEntity(
                    name = "Cash",
                    note = "default account",
                    type = AccountType.DEFAULT,
                    amount = 0.0,
                    color = PrimaryGreen.toHex()
                )
            )

        } else {
            Log.d(TAG, "default account already exists")
        }
    }
}