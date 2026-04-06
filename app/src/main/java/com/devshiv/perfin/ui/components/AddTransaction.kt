package com.devshiv.perfin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devshiv.perfin.data.local.entity.TransactionEntity
import com.devshiv.perfin.ui.theme.PrimaryGreen
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "add transaction" else "edit transaction") },
        text = {

            Column {

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        amountError = null
                    },
                    label = { Text("amount") },
                    isError = amountError != null
                )

                amountError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(Modifier.height(8.dp))

                Row {
                    TransactionType.entries.forEach {
                        Text(
                            it.name.lowercase(),
                            modifier = Modifier
                                .padding(6.dp)
                                .clickable { type = it }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text("account")
                accounts.forEach {
                    Text(
                        it.name,
                        modifier = Modifier.clickable { accountId = it.id }
                    )
                }
                accountError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }

                if (type == TransactionType.TRANSFER) {
                    Text("to account")
                    accounts.forEach {
                        Text(
                            it.name,
                            modifier = Modifier.clickable { toAccountId = it.id }
                        )
                    }
                }
                transferError?.let {
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(Modifier.height(8.dp))

                Text("category")

                val filtered = categories.filter { it.type == type }

                LazyRow {

                    items(filtered.chunked(3)) { columnItems ->

                        Column(modifier = Modifier.padding(end = 12.dp)) {

                            columnItems.forEach { cat ->

                                val isSelected = cat.id == categoryId

                                Row(
                                    modifier = Modifier
                                        .padding(vertical = 6.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (isSelected)
                                                Color(android.graphics.Color.parseColor(cat.color ?: "#E0F2F1"))
                                            else Color.White
                                        )
                                        .clickable { categoryId = cat.id }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    cat.icon?.let {
                                        Icon(
                                            imageVector = getCategoryIcon(it),
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else PrimaryGreen
                                        )
                                    }

                                    Spacer(Modifier.width(6.dp))

                                    Text(
                                        cat.name,
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(note, { note = it }, label = { Text("note") })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {

                    val amt = amount.toDoubleOrNull()

                    amountError = null
                    accountError = null
                    transferError = null

                    if (amt == null || amt <= 0) {
                        amountError = "enter valid amount"
                        return@TextButton
                    }

                    if (accountId == null) {
                        accountError = "select account"
                        return@TextButton
                    }

                    if (type == TransactionType.TRANSFER) {

                        if (toAccountId == null) {
                            transferError = "select destination account"
                            return@TextButton
                        }

                        if (accountId == toAccountId) {
                            transferError = "accounts must be different"
                            return@TextButton
                        }
                    }

                    onDismiss()
                }
            ) {
                Text("save", color = PrimaryGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("cancel")
            }
        }
    )
}