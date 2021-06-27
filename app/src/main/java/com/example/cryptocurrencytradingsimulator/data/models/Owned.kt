package com.example.cryptocurrencytradingsimulator.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Owned(
    @PrimaryKey(autoGenerate = true)
    val pk: Long = 0,
    val cryptoId: String = "",
    var amount: Double = 0.0
)
