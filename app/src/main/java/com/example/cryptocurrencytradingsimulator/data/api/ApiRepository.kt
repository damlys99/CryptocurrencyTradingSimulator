package com.example.cryptocurrencytradingsimulator.data.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cryptocurrencytradingsimulator.data.dao.FavoriteDao
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.CryptoChartData
import com.example.cryptocurrencytradingsimulator.data.models.Favorite
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
) {
    suspend fun getAllCrypto(): List<Crypto> = apiService.getAllCrypto("usd", "market_cap_desc", 250, 1, false, "24h")
    suspend fun getCrypto(ids: String): List<Crypto> = apiService.getCrypto("usd", ids, "1h,24h,7d,14d,30d,200d,1y")
    suspend fun getHistoricalData(id: String, from: Long, to: Long): CryptoChartData = apiService.getHistoricalData(id, "usd", from, to)
    fun getAllFavorites(): List<Favorite> = favoriteDao.getAllFavorites()
    fun addOrDeleteFavorite(isChecked: Boolean, favorite: Favorite) = run {
        if (isChecked) {
            favoriteDao.insert(favorite)
        } else {
            favoriteDao.deleteFavorite(favorite.id)
        }
    }
}