package com.devshiv.perfin.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionDetailsHelper(

    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)