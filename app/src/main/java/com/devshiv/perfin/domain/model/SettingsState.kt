package com.devshiv.perfin.domain.model

data class SettingsState(
    val isDarkMode: Boolean = false,
    val currency: String = "INR",
    val appLockEnabled: Boolean = false
)