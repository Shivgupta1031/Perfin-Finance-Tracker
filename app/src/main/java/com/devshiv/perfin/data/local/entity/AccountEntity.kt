package com.devshiv.perfin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devshiv.perfin.utils.AccountType

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val note: String,
    val type: AccountType = AccountType.DEFAULT,
    val amount: Double = 0.0,

    val color: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)