package com.devshiv.perfin

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.devshiv.perfin.data.local.database.PerFinDatabase
import com.devshiv.perfin.ui.navigation.NavGraph
import com.devshiv.perfin.ui.theme.PerFinTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}

@Composable
fun PerFinApp() {
    PerFinTheme {
        val navController = rememberNavController()
        NavGraph(navController = navController)
    }
}