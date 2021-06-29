package com.example.cryptocurrencytradingsimulator.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Direction{
    GREATER_THAN,  LESSER_THAN
}

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val pk: Long = 0,
    val id: String,
    val price: Double,
    val comp: Direction
)
