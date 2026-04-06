package com.devshiv.perfin.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devshiv.perfin.data.local.dao.AccountDao
import com.devshiv.perfin.data.local.dao.CategoryDao
import com.devshiv.perfin.data.local.dao.SettingsDao
import com.devshiv.perfin.data.local.dao.TransactionDao
import com.devshiv.perfin.data.local.entity.AccountEntity
import com.devshiv.perfin.data.local.entity.CategoryEntity
import com.devshiv.perfin.data.local.entity.TransactionEntity
import com.devshiv.perfin.utils.Constants
import com.devshiv.perfin.data.local.database.Converters
import com.devshiv.perfin.data.local.entity.SettingsEntity
import com.devshiv.perfin.di.DatabaseSeederEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        SettingsEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PerFinDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun settingsDao(): SettingsDao

}