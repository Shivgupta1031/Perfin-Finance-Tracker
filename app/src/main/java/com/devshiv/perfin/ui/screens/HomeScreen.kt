package com.devshiv.perfin.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.devshiv.perfin.ui.theme.BackgroundEnd
import com.devshiv.perfin.ui.theme.BackgroundStart
import com.devshiv.perfin.ui.theme.Gold
import com.devshiv.perfin.ui.theme.PrimaryGreen
import com.devshiv.perfin.R
import com.devshiv.perfin.data.local.entity.TransactionDetailsHelper
import com.devshiv.perfin.ui.components.AddEditTransactionDialog
import com.devshiv.perfin.ui.navigation.Routes
import com.devshiv.perfin.ui.viewmodel.MainViewModel
import com.devshiv.perfin.utils.TransactionType

@Composable
fun HomeBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BackgroundStart, BackgroundEnd)
                )
            )
    )
}

@Composable
fun HomeScreen(nav: NavController, vm: MainViewModel = hiltViewModel()) {

    val income by vm.income.collectAsState()
    val expense by vm.expense.collectAsState()
    val transactions by vm.transactions.collectAsState()
    val accounts by vm.accounts.collectAsState()

    val balance = accounts.sumOf { it.amount }

    Box {

        HomeBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item {
                GreetingHeader()
            }

            item {
                BalanceCard(balance, income, expense)
            }

            item {
                Spacer(Modifier.height(16.dp))
                InsightsCard()
            }

            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(transactions.take(5)) { item ->
                ModernTransactionItem(item, vm)
            }
        }
    }
}

@Composable
fun ModernTransactionItem(
    item: TransactionDetailsHelper,
    vm: MainViewModel
) {

    var showEdit by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(item.category?.name ?: "other", fontWeight = FontWeight.SemiBold)

                Text(
                    item.account.name,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Row {
                    Text(
                        "edit",
                        color = PrimaryGreen,
                        modifier = Modifier.clickable { showEdit = true }
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        "delete",
                        color = Color.Red,
                        modifier = Modifier.clickable { showDelete = true }
                    )
                }
            }

            Text(
                "₹${item.transaction.amount.toInt()}",
                color = when (item.transaction.type) {
                    TransactionType.INCOME -> Color(0xFF2E7D32)
                    TransactionType.EXPENSE -> Color.Red
                    TransactionType.TRANSFER -> Color.Blue
                },
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showDelete) {
        DeleteDialog(
            onConfirm = {
                vm.deleteTransaction(item.transaction)
                showDelete = false
            },
            onDismiss = { showDelete = false }
        )
    }

    if (showEdit) {
        AddEditTransactionDialog(
            existing = item.transaction,
            vm = vm,
            onDismiss = { showEdit = false }
        )
    }
}

@Composable
fun GreetingHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "PerFin - Hey 👋",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )

        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun BalanceCard(balance: Double, income: Double, expense: Double) {

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text("Total Balance", color = Color.Gray)

                    Text(
                        "₹%,.0f".format(balance),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("↑ Income  ₹${income.toInt()}", color = Color(0xFF2E7D32))
                    Text("↓ Expense ₹${expense.toInt()}", color = Color.Red)
                }

                DonutChart(income, expense)
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Weekly", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                Text("|", color = Color.Gray)
                Text("Monthly", color = Color.Gray)
            }
        }
    }
}

@Composable
fun DonutChart(income: Double, expense: Double) {

    val total = income + expense
    if (total == 0.0) return

    val incomePercent = (income / total * 100).toFloat()
    val expensePercent = (expense / total * 100).toFloat()

    val values = listOf(incomePercent, expensePercent)
    val colors = listOf(
        Color(0xFF2E7D32),
        Color.Red
    )

    Canvas(modifier = Modifier.size(120.dp)) {

        var startAngle = -90f

        values.forEachIndexed { index, value ->
            val sweep = value / 100f * 360f

            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = 30f, cap = StrokeCap.Round)
            )

            startAngle += sweep
        }
    }
}

@Composable
fun InsightsCard() {

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                "You saved ₹500 this week 🎉",
                fontWeight = FontWeight.Medium
            )
        }
    }
}
