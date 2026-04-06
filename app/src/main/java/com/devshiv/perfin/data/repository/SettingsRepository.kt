package com.devshiv.perfin.data.repository

import com.devshiv.perfin.data.local.dao.SettingsDao
import com.devshiv.perfin.data.local.entity.SettingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dao: SettingsDao
) {

    fun getSettings() = dao.getSettings()

    suspend fun save(settings: SettingsEntity) =
        withContext(Dispatchers.IO) {
            dao.insert(settings)
        }

    suspend fun update(transform: (SettingsEntity) -> SettingsEntity) =
        withContext(Dispatchers.IO) {

            val current = dao.getSettingsOnce() ?: SettingsEntity()

            val updated = transform(current)

            dao.insert(updated)
        }

    suspend fun setOnboardingShown() {
        update { it.copy(onboardingShown = true) }
    }

    suspend fun toggleDarkMode() {
        update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    suspend fun toggleAppLock() {
        update { it.copy(appLockEnabled = !it.appLockEnabled) }
    }

    suspend fun setCurrency(currency: String) {
        update { it.copy(currency = currency) }
    }

    suspend fun ensureDefault() =
        withContext(Dispatchers.IO) {

            val existing = dao.getSettingsOnce()

            if (existing == null) {
                dao.insert(SettingsEntity())
            }
        }
}