package com.example.cryptocurrencytradingsimulator.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val pk: Long = 0,
    val type: TransactionType,
    val cryptoId: String = "",
    val income: Double = 0.0,
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val balance: Double = 0.0,
    val owned: Double = 0.0,
    val date: Long = 0L
)


enum class TransactionType{
    BUY, SELL
}