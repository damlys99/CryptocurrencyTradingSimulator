package com.example.cryptocurrencytradingsimulator.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cryptocurrencytradingsimulator.data.models.Favorite

@Dao
interface FavoriteDao: BaseDao<Favorite> {

    @Query("SELECT * FROM Favorite")
    fun getAllFavorites(): List<Favorite>

    @Query("DELETE FROM Favorite WHERE id = :id")
    fun deleteFavorite(id: String)
}