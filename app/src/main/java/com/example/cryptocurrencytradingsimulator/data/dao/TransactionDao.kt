package com.example.cryptocurrencytradingsimulator.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cryptocurrencytradingsimulator.data.models.Transaction

@Dao
interface TransactionDao: BaseDao<Transaction> {

    @Query("SELECT * FROM [Transaction] WHERE cryptoId=:cryptoId ORDER BY date DESC")
    fun getTransactions(cryptoId: String): List<Transaction>

    @Query("SELECT * FROM [Transaction] ORDER BY DATE ASC")
    fun getAllTransactions(): List<Transaction>
}