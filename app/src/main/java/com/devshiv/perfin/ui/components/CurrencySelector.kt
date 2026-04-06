package com.devshiv.perfin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CurrencySelector(
    selected: String,
    onSelect: (String) -> Unit
) {

    val currencies = listOf("INR", "USD", "EUR")

    Column {

        Text("Currency", fontWeight = FontWeight.SemiBold)

        currencies.forEach {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(it) }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(it)

                if (it == selected) {
                    Text("✓")
                }
            }
        }
    }
}