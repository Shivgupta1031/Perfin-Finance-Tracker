package com.devshiv.perfin.di

import com.devshiv.perfin.data.local.database.DatabaseSeeder
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DatabaseSeederEntryPoint {
    fun seeder(): DatabaseSeeder
}