package com.devshiv.perfin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.devshiv.perfin.data.repository.SettingsRepository
import com.devshiv.perfin.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val settingsRepo: SettingsRepository
) : ViewModel() {

    val startDestination = settingsRepo.getSettings()
        .map { settings ->
            if (settings?.onboardingShown == true) {
                Routes.HOME
            } else {
                Routes.ONBOARDING
            }
        }
}