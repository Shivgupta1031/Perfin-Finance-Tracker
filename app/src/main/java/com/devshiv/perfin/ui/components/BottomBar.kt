package com.devshiv.perfin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.devshiv.perfin.ui.navigation.Routes
import com.devshiv.perfin.ui.theme.PrimaryGreen

@Composable
fun BottomBar(nav: NavController) {

    val currentRoute =
        nav.currentBackStackEntryAsState().value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.White, RoundedCornerShape(30.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomItem(
                "Home",
                Icons.Default.Home,
                currentRoute == Routes.HOME
            ) {
                nav.navigate(Routes.HOME) {
                    popUpTo(Routes.HOME)
                    launchSingleTop = true
                }
            }

            BottomItem(
                "Transactions",
                Icons.Default.List,
                currentRoute == Routes.TRANSACTIONS
            ) {
                nav.navigate(Routes.TRANSACTIONS) {
                    launchSingleTop = true
                }
            }

            Spacer(modifier = Modifier.width(50.dp))

            BottomItem(
                "Accounts",
                Icons.Default.AccountBalance,
                currentRoute == Routes.ACCOUNTS
            ) {
                nav.navigate(Routes.ACCOUNTS) {
                    launchSingleTop = true
                }
            }

            BottomItem(
                "Settings",
                Icons.Default.Settings,
                currentRoute == Routes.SETTINGS
            ) {
                nav.navigate(Routes.SETTINGS) {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun BottomItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = if (selected) PrimaryGreen else Color.Gray
        )

        Text(
            title,
            fontSize = 12.sp,
            color = if (selected) PrimaryGreen else Color.Gray
        )
    }
}