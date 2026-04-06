package com.devshiv.perfin.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AboutSection() {

    Column {

        Text("About", fontWeight = FontWeight.SemiBold)

        Text("PerFin v1.0")
        Text("Offline personal finance companion")
    }
}