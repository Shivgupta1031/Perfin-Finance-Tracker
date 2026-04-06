package com.devshiv.perfin.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devshiv.perfin.data.local.dao.AccountDao
import com.devshiv.perfin.data.local.dao.CategoryDao
import com.devshiv.perfin.data.local.dao.SettingsDao
import com.devshiv.perfin.data.local.dao.TransactionDao
import com.devshiv.perfin.data.local.database.PerFinDatabase
import com.devshiv.perfin.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PerFinDatabase {

        return Room.databaseBuilder(
            context,
            PerFinDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    CoroutineScope(Dispatchers.IO).launch {

                        val entryPoint = EntryPointAccessors.fromApplication(
                            context,
                            DatabaseSeederEntryPoint::class.java
                        )

                        entryPoint.seeder().seed()
                    }
                }
            })
            .build()
    }

    @Provides
    fun provideTransactionDao(db: PerFinDatabase): TransactionDao =
        db.transactionDao()

    @Provides
    fun provideSettingsDao(db: PerFinDatabase): SettingsDao =
        db.settingsDao()

    @Provides
    fun provideAccountDao(db: PerFinDatabase): AccountDao =
        db.accountDao()

    @Provides
    fun provideCategoryDao(db: PerFinDatabase): CategoryDao =
        db.categoryDao()
}