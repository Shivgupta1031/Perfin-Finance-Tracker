package com.devshiv.perfin.data.local.database

import androidx.room.TypeConverter
import com.devshiv.perfin.utils.TransactionType

class Converters {

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType =
        TransactionType.valueOf(value)
}