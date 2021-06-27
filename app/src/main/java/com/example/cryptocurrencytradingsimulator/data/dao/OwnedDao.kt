package com.example.cryptocurrencytradingsimulator.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cryptocurrencytradingsimulator.data.models.Owned

@Dao
interface OwnedDao: BaseDao<Owned> {

    @Query("SELECT * FROM Owned")
    fun getAllOwned(): List<Owned>

    @Query("SELECT * FROM OWNED WHERE cryptoId=:cryptoId")
    fun getOwned(cryptoId: String): Owned?
}