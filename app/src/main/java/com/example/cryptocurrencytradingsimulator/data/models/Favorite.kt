package com.example.cryptocurrencytradingsimulator.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val pk: Long = 0,
    val id: String
)