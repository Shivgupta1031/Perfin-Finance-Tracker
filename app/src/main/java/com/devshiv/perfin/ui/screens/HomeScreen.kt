package com.devshiv.perfin.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.devshiv.perfin.ui.theme.BackgroundEnd
import com.devshiv.perfin.ui.theme.BackgroundStart
import com.devshiv.perfin.ui.theme.PrimaryGreen
import com.devshiv.perfin.R
import com.devshiv.perfin.data.local.entity.TransactionDetailsHelper
import com.devshiv.perfin.ui.components.AddEditTransactionDialog
import com.devshiv.perfin.ui.theme.CardBackground
import com.devshiv.perfin.ui.theme.ExpenseRed
import com.devshiv.perfin.ui.theme.IncomeGreen
import com.devshiv.perfin.ui.theme.TextPrimary
import com.devshiv.perfin.ui.theme.TextSecondary
import com.devshiv.perfin.ui.viewmodel.MainViewModel
import com.devshiv.perfin.utils.Helpers.getCategoryIcon
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
                InsightsCard(income, expense)
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

    val type = item.transaction.type
    val amount = item.transaction.amount

    val accentColor = when (type) {
        TransactionType.INCOME -> IncomeGreen
        TransactionType.EXPENSE -> ExpenseRed
        TransactionType.TRANSFER -> PrimaryGreen
    }

    val typeIcon = when (type) {
        TransactionType.INCOME -> Icons.Rounded.ArrowUpward
        TransactionType.EXPENSE -> Icons.Rounded.ArrowDownward
        TransactionType.TRANSFER -> Icons.Rounded.CompareArrows
    }
    val amountPrefix = when (type) {
        TransactionType.INCOME -> "+"
        TransactionType.EXPENSE -> "-"
        TransactionType.TRANSFER -> ""
    }

    val catColor = runCatching {
        Color(android.graphics.Color.parseColor(item.category?.color ?: "#2BB673"))
    }.getOrDefault(PrimaryGreen)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(catColor.copy(alpha = 0.15f), catColor.copy(alpha = 0.25f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                val icon = item.category?.icon?.let { getCategoryIcon(it) } ?: typeIcon
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = catColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.category?.name?.replaceFirstChar { it.uppercase() } ?: "Other",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(accentColor.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = typeIcon,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(9.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                color = accentColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = item.account.name,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                item.transaction.note?.takeIf { it.isNotBlank() }?.let { noteText ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = noteText,
                        fontSize = 11.sp,
                        color = TextSecondary.copy(alpha = 0.7f),
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$amountPrefix₹${"%,.0f".format(amount)}",
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen.copy(alpha = 0.1f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { showEdit = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(ExpenseRed.copy(alpha = 0.1f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { showDelete = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint = ExpenseRed,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(accentColor.copy(alpha = 0.0f), accentColor.copy(alpha = 0.5f))
                    )
                )
        )
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            containerColor = CardBackground,
            shape = RoundedCornerShape(24.dp),
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(ExpenseRed.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            tint = ExpenseRed,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Delete Transaction?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "This will permanently remove",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "${item.category?.name ?: "this transaction"} · $amountPrefix₹${"%,.0f".format(amount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ExpenseRed
                    )
                    Text(
                        text = "This action cannot be undone.",
                        fontSize = 13.sp,
                        color = TextSecondary.copy(alpha = 0.7f)
                    )
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        vm.deleteTransaction(item.transaction)
                        showDelete = false
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = ExpenseRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Yes, Delete", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDelete = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", color = TextSecondary)
                }
            }
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
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )

                   Spacer(Modifier.height(12.dp))
//
//                   Text("↑ Income  ₹${income.toInt()}", color = Color(0xFF2E7D32))
//                   Text("↓ Expense ₹${expense.toInt()}", color = Color.Red)
                }

//               DonutChart(income, expense)
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    "↑ Income ₹${income.toInt()}",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold
                )
                Text("|", color = Color.Gray)
                Text("↓ Expense ₹${expense.toInt()}", color = Color.Red)
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
fun InsightsCard(income: Double, expense: Double) {

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

            if ((income - expense) > 0) {
                Text(
                    "You saved ₹${income - expense} this week 🎉",
                    fontWeight = FontWeight.Medium
                )
            } else {
                Text(
                    "You spent ₹${income - expense} this week 🎉",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
