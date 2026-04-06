package com.devshiv.perfin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devshiv.perfin.utils.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val type: TransactionType,
    val icon: String? = null,
    val color: String? = null
)