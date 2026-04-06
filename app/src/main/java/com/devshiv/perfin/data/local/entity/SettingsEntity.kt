package com.devshiv.perfin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(

    @PrimaryKey
    val id: Int = 1,

    val isDarkMode: Boolean = false,
    val appLockEnabled: Boolean = false,
    val currency: String = "INR",

    val onboardingShown: Boolean = false
)