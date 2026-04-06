package com.devshiv.perfin.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devshiv.perfin.data.local.entity.TransactionEntity
import com.devshiv.perfin.ui.theme.Background
import com.devshiv.perfin.ui.theme.CardBackground
import com.devshiv.perfin.ui.theme.DarkGreen
import com.devshiv.perfin.ui.theme.ExpenseRed
import com.devshiv.perfin.ui.theme.IncomeGreen
import com.devshiv.perfin.ui.theme.LightGreen
import com.devshiv.perfin.ui.theme.PrimaryGreen
import com.devshiv.perfin.ui.theme.TextPrimary
import com.devshiv.perfin.ui.theme.TextSecondary
import com.devshiv.perfin.ui.viewmodel.MainViewModel
import com.devshiv.perfin.utils.Helpers.getCategoryIcon
import com.devshiv.perfin.utils.TransactionType

@Composable
fun AddEditTransactionDialog(
    existing: TransactionEntity? = null,
    vm: MainViewModel,
    onDismiss: () -> Unit
) {
    val accounts by vm.accounts.collectAsState()
    val categories by vm.categories.collectAsState()

    var amount by remember { mutableStateOf(existing?.amount?.toString() ?: "") }
    var type by remember { mutableStateOf(existing?.type ?: TransactionType.EXPENSE) }
    var accountId by remember { mutableStateOf(existing?.accountId ?: accounts.firstOrNull()?.id) }
    var toAccountId by remember { mutableStateOf(existing?.toAccountId) }
    var categoryId by remember { mutableStateOf(existing?.categoryId) }
    var note by remember { mutableStateOf(existing?.note ?: "") }

    var amountError by remember { mutableStateOf<String?>(null) }
    var accountError by remember { mutableStateOf<String?>(null) }
    var transferError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }

    val isEditing = existing != null

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardBackground,
        shape = RoundedCornerShape(28.dp),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(listOf(LightGreen, PrimaryGreen, DarkGreen))
                        )
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (isEditing) "Edit Transaction" else "Add Transaction",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (isEditing) "Update the details below" else "Fill in the details below",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                SectionLabel("Amount (₹)")
                OutlinedTextField(
                    value = amount,
                    onValueChange = {

                        if (it.matches(Regex("^\\d{0,10}(\\.\\d{0,2})?\$"))) {
                            amount = it
                            amountError = null
                        }
                    },
                    placeholder = { Text("0.00", color = TextSecondary) },
                    isError = amountError != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = Color(0xFFDDE8E3),
                        errorBorderColor = ExpenseRed,
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                ErrorMessage(amountError)

                Spacer(Modifier.height(16.dp))

                SectionLabel("Type")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Background)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TransactionType.entries.forEach { t ->
                        val isSelected = type == t
                        val bgColor by animateColorAsState(
                            targetValue = when {
                                !isSelected -> Color.Transparent
                                t == TransactionType.INCOME -> IncomeGreen
                                t == TransactionType.EXPENSE -> ExpenseRed
                                else -> PrimaryGreen
                            },
                            animationSpec = tween(200),
                            label = "typeBg"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) Color.White else TextSecondary,
                            animationSpec = tween(200),
                            label = "typeText"
                        )
                        val typeIcon = when (t) {
                            TransactionType.INCOME -> Icons.Rounded.ArrowUpward
                            TransactionType.EXPENSE -> Icons.Rounded.ArrowDownward
                            TransactionType.TRANSFER -> Icons.Rounded.CompareArrows
                        }
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(bgColor)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    type = t
                                    transferError = null
                                    categoryError = null
                                    categoryId = null
                                }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = typeIcon,
                                contentDescription = t.name,
                                tint = textColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = t.name.lowercase().replaceFirstChar { it.uppercase() },
                                color = textColor,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                SectionLabel("From Account")
                if (accounts.isEmpty()) {
                    EmptyStateChip("No accounts found. Please create one first.")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(accounts) { acc ->
                            val isSelected = acc.id == accountId
                            AccountChip(
                                name = acc.name,
                                isSelected = isSelected,
                                onClick = {
                                    accountId = acc.id
                                    accountError = null
                                    if (transferError != null) transferError = null
                                }
                            )
                        }
                    }
                }
                ErrorMessage(accountError)

                AnimatedVisibility(
                    visible = type == TransactionType.TRANSFER,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(Modifier.height(12.dp))
                        SectionLabel("To Account")
                        if (accounts.isEmpty()) {
                            EmptyStateChip("No accounts found.")
                        } else {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(accounts) { acc ->
                                    val isSelected = acc.id == toAccountId
                                    AccountChip(
                                        name = acc.name,
                                        isSelected = isSelected,
                                        accentColor = DarkGreen,
                                        onClick = {
                                            toAccountId = acc.id
                                            transferError = null
                                        }
                                    )
                                }
                            }
                        }
                        ErrorMessage(transferError)
                    }
                }

                Spacer(Modifier.height(16.dp))

                val filteredCategories = categories.filter { it.type == type }
                AnimatedVisibility(visible = type != TransactionType.TRANSFER) {
                    Column {
                        SectionLabel("Category")
                        if (filteredCategories.isEmpty()) {
                            EmptyStateChip("No categories for this type.")
                        } else {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(filteredCategories.chunked(3)) { columnItems ->
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        columnItems.forEach { cat ->
                                            val isSelected = cat.id == categoryId
                                            val catColor = runCatching {
                                                Color(
                                                    android.graphics.Color.parseColor(
                                                        cat.color ?: "#2BB673"
                                                    )
                                                )
                                            }.getOrDefault(PrimaryGreen)

                                            Row(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(20.dp))
                                                    .background(
                                                        if (isSelected) catColor.copy(alpha = 0.15f)
                                                        else Background
                                                    )
                                                    .border(
                                                        width = if (isSelected) 1.5.dp else 1.dp,
                                                        color = if (isSelected) catColor else Color(
                                                            0xFFDDE8E3
                                                        ),
                                                        shape = RoundedCornerShape(20.dp)
                                                    )
                                                    .clickable(
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        indication = null
                                                    ) {
                                                        categoryId = cat.id
                                                        categoryError = null
                                                    }
                                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                cat.icon?.let {
                                                    Icon(
                                                        imageVector = getCategoryIcon(it),
                                                        contentDescription = null,
                                                        tint = if (isSelected) catColor else TextSecondary,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(Modifier.width(6.dp))
                                                }
                                                Text(
                                                    cat.name,
                                                    color = if (isSelected) catColor else TextPrimary,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                                )
                                                if (isSelected) {
                                                    Spacer(Modifier.width(4.dp))
                                                    Icon(
                                                        imageVector = Icons.Rounded.CheckCircle,
                                                        contentDescription = null,
                                                        tint = catColor,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ErrorMessage(categoryError)
                        Spacer(Modifier.height(16.dp))
                    }
                }

                SectionLabel("Note (Optional)")
                OutlinedTextField(
                    value = note,
                    onValueChange = {
                        if (it.length <= 200) note = it
                    },
                    placeholder = { Text("Add a short note…", color = TextSecondary) },
                    singleLine = false,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = Color(0xFFDDE8E3),
                        focusedLabelColor = PrimaryGreen,
                        cursorColor = PrimaryGreen
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "${note.length}/200",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.End
                )
            }
        },
        confirmButton = {
            ElevatedButton(
                onClick = {
                    amountError = null
                    accountError = null
                    transferError = null
                    categoryError = null

                    val amt = amount.toDoubleOrNull()

                    when {
                        amount.isBlank() -> {
                            amountError = "Amount is required"
                            return@ElevatedButton
                        }

                        amt == null || amt <= 0 -> {
                            amountError = "Please enter a valid amount greater than 0"
                            return@ElevatedButton
                        }

                        amt > 10_000_000 -> {
                            amountError = "Amount seems too large. Please check."
                            return@ElevatedButton
                        }
                    }

                    if (accountId == null) {
                        accountError = "Please select an account"
                        return@ElevatedButton
                    }

                    if (type == TransactionType.TRANSFER) {
                        when {
                            toAccountId == null -> {
                                transferError = "Please select a destination account"
                                return@ElevatedButton
                            }

                            accountId == toAccountId -> {
                                transferError = "Source and destination accounts must be different"
                                return@ElevatedButton
                            }
                        }
                    }

                    if (type != TransactionType.TRANSFER && categoryId == null) {
                        categoryError = "Please select a category"
                        return@ElevatedButton
                    }

                    if (isEditing) {
                        vm.updateTransaction(
                            TransactionEntity(
                                id = existing.id,
                                amount = amount.toDouble(),
                                type = type,
                                categoryId = categoryId,
                                accountId = accountId!!,
                                toAccountId = toAccountId,
                                note = note
                            )
                        )
                    } else {
                        vm.addTransaction(
                            TransactionEntity(
                                amount = amount.toDouble(),
                                type = type,
                                categoryId = categoryId,
                                accountId = accountId!!,
                                toAccountId = toAccountId,
                                note = note
                            )
                        )
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (isEditing) "Update" else "Save Transaction",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Cancel",
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ErrorMessage(error: String?) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        error?.let {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.ErrorOutline,
                    contentDescription = null,
                    tint = ExpenseRed,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = it,
                    color = ExpenseRed,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun AccountChip(
    name: String,
    isSelected: Boolean,
    accentColor: Color = PrimaryGreen,
    onClick: () -> Unit
) {
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        label = "chipBorder"
    )
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.1f) else Background)
            .border(
                width = borderWidth,
                color = if (isSelected) accentColor else Color(0xFFDDE8E3),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isSelected) accentColor else TextSecondary.copy(alpha = 0.4f))
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = name,
            color = if (isSelected) accentColor else TextPrimary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun EmptyStateChip(message: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Background)
            .border(1.dp, Color(0xFFDDE8E3), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(message, color = TextSecondary, fontSize = 13.sp)
    }
}