package com.devshiv.perfin.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

object Helpers {

    @Composable
    fun getCategoryIcon(icon: String): ImageVector {

        return when (icon) {
            CategoryIcons.FOOD -> Icons.Default.Fastfood
            CategoryIcons.TRAVEL -> Icons.Default.Flight
            CategoryIcons.SHOPPING -> Icons.Default.ShoppingCart
            CategoryIcons.BILLS -> Icons.Default.Receipt
            CategoryIcons.HEALTH -> Icons.Default.MedicalServices
            CategoryIcons.ENTERTAINMENT -> Icons.Default.Movie
            CategoryIcons.EDUCATION -> Icons.Default.School
            CategoryIcons.TRANSPORT -> Icons.Default.DirectionsCar
            CategoryIcons.SALARY -> Icons.Default.AttachMoney
            CategoryIcons.BUSINESS -> Icons.Default.Business
            else -> Icons.Default.Category
        }
    }
}