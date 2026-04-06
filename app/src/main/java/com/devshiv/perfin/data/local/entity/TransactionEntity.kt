package com.devshiv.perfin.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devshiv.perfin.utils.TransactionType

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("accountId"), Index("categoryId")]
)

data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val amount: Double,
    val type: TransactionType,

    val accountId: Int,
    val toAccountId: Int? = null,

    val categoryId: Int?,
    val note: String?,

    val date: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)