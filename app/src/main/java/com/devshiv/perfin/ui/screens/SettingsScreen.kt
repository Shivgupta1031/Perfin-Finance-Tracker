package com.devshiv.perfin.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.devshiv.perfin.ui.components.CurrencySelector
import com.devshiv.perfin.ui.components.DangerZone
import com.devshiv.perfin.ui.components.SettingsToggleItem
import com.devshiv.perfin.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(vm: SettingsViewModel = hiltViewModel()) {

    val state by vm.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Spacer(Modifier.height(16.dp))
            SettingsToggleItem(
                title = "Dark Mode",
                checked = state.isDarkMode,
                onCheckedChange = { vm.toggleDarkMode() }
            )
        }

        item {
            SettingsToggleItem(
                title = "App Lock",
                checked = state.appLockEnabled,
                onCheckedChange = { vm.toggleAppLock() }
            )
        }

        item {
            Spacer(Modifier.height(16.dp))
            CurrencySelector(
                selected = state.currency,
                onSelect = { vm.setCurrency(it) }
            )
        }

        item {
            Spacer(Modifier.height(16.dp))
            DangerZone()
        }

        item {
            Spacer(Modifier.height(16.dp))
            AboutSection()
        }
    }
}