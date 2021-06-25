package com.example.cryptocurrencytradingsimulator.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cryptocurrencytradingsimulator.data.models.Transaction

@Dao
interface TransactionDao: BaseDao<Transaction> {

    @Query("SELECT * FROM [Transaction] WHERE cryptoId=:cryptoId")
    fun getTransactions(cryptoId: String): List<Transaction>
}