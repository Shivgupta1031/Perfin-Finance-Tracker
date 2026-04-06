package com.devshiv.perfin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devshiv.perfin.data.repository.SettingsRepository
import com.devshiv.perfin.ui.screens.AccountsScreen
import com.devshiv.perfin.ui.screens.MainContainer
import com.devshiv.perfin.ui.screens.OnboardingScreen
import com.devshiv.perfin.ui.screens.SettingsScreen
import com.devshiv.perfin.ui.screens.SplashScreen
import com.devshiv.perfin.ui.screens.TransactionScreen
import kotlinx.coroutines.flow.first

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Routes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            MainContainer()
        }

        composable(Routes.TRANSACTIONS) {
            TransactionScreen()
        }

        composable(Routes.ACCOUNTS) {
            AccountsScreen()
        }

        composable(Routes.SETTINGS) {
            SettingsScreen()
        }

//        composable(Routes.ADD_TRANSACTION) {
//            AddTransactionScreen(navController)
//        }
    }
}