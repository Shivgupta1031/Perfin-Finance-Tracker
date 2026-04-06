package com.devshiv.perfin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.perfin.data.local.entity.SettingsEntity
import com.devshiv.perfin.data.repository.SettingsRepository
import com.devshiv.perfin.domain.model.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: SettingsRepository
) : ViewModel() {

    val state = repo.getSettings()
        .map { it ?: SettingsEntity() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            SettingsEntity()
        )

    init {
        viewModelScope.launch {
            repo.ensureDefault()
        }
    }

    fun setOnboardingShown() {
        viewModelScope.launch {
            repo.setOnboardingShown()
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            repo.toggleDarkMode()
        }
    }

    fun toggleAppLock() {
        viewModelScope.launch {
            repo.toggleAppLock()
        }
    }

    fun setCurrency(currency: String) {
        viewModelScope.launch {
            repo.setCurrency(currency)
        }
    }
}