package com.devshiv.perfin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.devshiv.perfin.data.local.entity.AccountEntity
import com.devshiv.perfin.ui.theme.PrimaryGreen
import com.devshiv.perfin.ui.viewmodel.AccountViewModel
import com.devshiv.perfin.ui.viewmodel.MainViewModel
import com.devshiv.perfin.utils.AccountType

@Composable
fun AccountsScreen(vm: AccountViewModel = hiltViewModel()) {

    val accounts by vm.accounts.collectAsState()

    var showAdd by remember { mutableStateOf(false) }

    Box {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item {
                Text(
                    "Accounts",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                AccountsBalanceCards(accounts)
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            items(accounts) { account ->
                AccountItem(
                    account = account,
                    vm = vm
                )
            }
        }

        FloatingActionButton(
            onClick = { showAdd = true },
            containerColor = PrimaryGreen,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, null)
        }
    }

    if (showAdd) {
        AddAccountDialog(
            onAdd = {
                vm.addAccountEntity(it)
                showAdd = false
            },
            onDismiss = { showAdd = false }
        )
    }
}

@Composable
fun AccountsBalanceCards(accounts: List<AccountEntity>) {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        items(accounts) { account ->

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .width(160.dp)
                    .padding(end = 12.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(account.name, fontWeight = FontWeight.SemiBold)

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "₹${account.amount.toInt()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AccountItem(
    account: AccountEntity,
    vm: AccountViewModel
) {

    val stats by vm.getAccountStats(account.id)
        .collectAsState(initial = 0.0 to 0.0)

    val income = stats.first
    val expense = stats.second

    var showEdit by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(account.name, fontWeight = FontWeight.SemiBold)

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

            Spacer(Modifier.height(8.dp))

            Text("Balance ₹${account.amount}", color = Color.Gray)

            Text("Income ₹${income.toInt()}", color = Color(0xFF2E7D32))
            Text("Expense ₹${expense.toInt()}", color = Color.Red)
        }
    }

    if (showEdit) {
        EditAccountDialog(
            account = account,
            onUpdate = {
                vm.updateAccount(it)
                showEdit = false
            },
            onDismiss = { showEdit = false }
        )
    }

    if (showDelete) {
        DeleteDialog(
            onConfirm = {
                vm.delete(account)
                showDelete = false
            },
            onDismiss = { showDelete = false }
        )
    }
}

@Composable
fun DeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("delete", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("cancel")
            }
        },
        title = { Text("Delete Account") },
        text = { Text("transactions will be moved to default account") }
    )
}

@Composable
fun EditAccountDialog(
    account: AccountEntity,
    onUpdate: (AccountEntity) -> Unit,
    onDismiss: () -> Unit
) {

    var name by remember { mutableStateOf(account.name) }
    var note by remember { mutableStateOf(account.note) }
    var type by remember { mutableStateOf(account.type) }
    var color by remember { mutableStateOf(account.color ?: "#4CAF50") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("edit account") },
        text = {

            Column {

                OutlinedTextField(name, { name = it }, label = { Text("name") })

                OutlinedTextField(note, { note = it }, label = { Text("note") })

                Spacer(Modifier.height(8.dp))

                Row {
                    AccountType.entries.forEach { typeItem ->
                        Text(
                            text = typeItem.name.lowercase(),
                            modifier = Modifier
                                .padding(6.dp)
                                .clickable { type = typeItem }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row {
                    listOf("#4CAF50", "#2196F3", "#FF9800").forEach {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color(android.graphics.Color.parseColor(it)))
                                .clickable { color = it }
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onUpdate(
                        account.copy(
                            name = name,
                            note = note,
                            type = type,
                            color = color
                        )
                    )
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

@Composable
fun AddAccountDialog(
    onAdd: (AccountEntity) -> Unit,
    onDismiss: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(AccountType.DEFAULT) }
    var amount by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("#4CAF50") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("add account") },
        text = {

            Column {

                OutlinedTextField(name, { name = it }, label = { Text("name") })

                OutlinedTextField(note, { note = it }, label = { Text("note") })

                OutlinedTextField(amount, { amount = it }, label = { Text("initial balance") })

                Spacer(Modifier.height(8.dp))

                Row {
                    AccountType.entries.forEach { typeItem ->
                        Text(
                            text = typeItem.name.lowercase(),
                            modifier = Modifier
                                .padding(6.dp)
                                .clickable { type = typeItem }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row {
                    listOf("#4CAF50", "#2196F3", "#FF9800").forEach {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color(android.graphics.Color.parseColor(it)))
                                .clickable { color = it }
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(
                            AccountEntity(
                                name = name,
                                note = note,
                                type = type,
                                amount = amount.toDoubleOrNull() ?: 0.0,
                                color = color
                            )
                        )
                    }
                }
            ) {
                Text("add", color = PrimaryGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("cancel")
            }
        }
    )
}

