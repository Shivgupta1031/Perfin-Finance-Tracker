package com.devshiv.perfin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.devshiv.perfin.R
import com.devshiv.perfin.domain.model.OnboardingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    val pages = listOf(
        OnboardingItem(
            "Welcome to PerFin",
            "Track your money,\nbuild better habits",
            R.drawable.onboarding_1
        ),
        OnboardingItem(
            "Track Every Rupee",
            "Add income, expenses, and\nmanage accounts easily",
            R.drawable.onboarding_2
        ),
        OnboardingItem(
            "Your Data Stays With You",
            "No internet. No tracking.\n100% private.",
            R.drawable.onboarding_3
        )
    )
}