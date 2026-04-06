package com.devshiv.perfin.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devshiv.perfin.ui.components.AddEditTransactionDialog
import com.devshiv.perfin.ui.components.BottomBar
import com.devshiv.perfin.ui.navigation.Routes
import com.devshiv.perfin.ui.theme.PrimaryGreen
import com.devshiv.perfin.ui.viewmodel.MainViewModel

@Composable
fun MainContainer(vm: MainViewModel = hiltViewModel()) {

    val navController = rememberNavController()

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomBar(navController)
        },
        floatingActionButton = {

            var showDialog by remember { mutableStateOf(false) }

            if (showDialog) {
                AddEditTransactionDialog(
                    vm = vm,
                    onDismiss = { showDialog = false }
                )
            }

            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = PrimaryGreen,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.HOME) {
                HomeScreen(navController)
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
        }
    }
}