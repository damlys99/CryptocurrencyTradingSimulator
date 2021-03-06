package com.example.cryptocurrencytradingsimulator.data.api

import com.example.cryptocurrencytradingsimulator.data.dao.FavoriteDao
import com.example.cryptocurrencytradingsimulator.data.dao.NotificationsDao
import com.example.cryptocurrencytradingsimulator.data.dao.OwnedDao
import com.example.cryptocurrencytradingsimulator.data.dao.TransactionDao
import com.example.cryptocurrencytradingsimulator.data.models.*
import com.example.cryptocurrencytradingsimulator.utils.CRYPTOS_IDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao,
    val notificationsDao: NotificationsDao,
    val transactionDao: TransactionDao,
    val ownedDao: OwnedDao
) {
    suspend fun getAllCrypto(): List<Crypto> = apiService.getAllCrypto(CRYPTOS_IDS, "usd", "market_cap_desc", 250, 1, false, "24h")
    suspend fun getCrypto(ids: String): List<Crypto> = apiService.getCrypto("usd", ids, "1h,24h,7d,14d,30d,200d,1y")
    suspend fun getHistoricalData(id: String, from: Long, to: Long): CryptoChartData = apiService.getHistoricalData(id, "usd", from, to)
    suspend fun getPrices(ids: String) = apiService.getPrices(ids =ids, vs_currency = "usd")
    fun getAllFavorites(): List<Favorite> = favoriteDao.getAllFavorites()
    fun addOrDeleteFavorite(isChecked: Boolean, favorite: Favorite) = run {
        if (isChecked) {
            favoriteDao.insert(favorite)
        } else {
            favoriteDao.deleteFavorite(favorite.id)
        }
    }

    fun getTransactions(cryptoId: String): List<Transaction> = transactionDao.getTransactions(cryptoId)
    fun getAllTransactions() = transactionDao.getAllTransactions()
    fun getAllOwned(): List<Owned> = ownedDao.getAllOwned()
    fun getOwned(cryptoId: String): Owned? = ownedDao.getOwned(cryptoId)
    fun getAllNotifications() = notificationsDao.getAllNotifications()
    fun getNotification(cryptoId: String) = notificationsDao.getNotification(cryptoId)

}