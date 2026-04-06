package com.devshiv.perfin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DangerZone() {

    Column {

        Text("Danger Zone", color = Color.Red, fontWeight = FontWeight.Bold)

        Text(
            "Reset All Data",
            color = Color.Red,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable {
                    // call repo clear db
                }
        )
    }
}