package com.example.cryptocurrencytradingsimulator.data.api

import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.CryptoChartData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets")
    suspend fun getAllCrypto(
        @Query("vs_currency") vs_currency: String,
        @Query("order") order: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean,
        @Query("price_change_percentage") price_change_percentage: String
    ): List<Crypto>

    @GET("coins/markets")
    suspend fun getCrypto(
        @Query("vs_currency") vs_currency: String,
        @Query("ids") ids: String,
        @Query("price_change_percentage") price_change_percentage: String
    ): List<Crypto>

    @GET("coins/{id}/market_chart/range")
    suspend fun getHistoricalData(
        @Path("id") id: String,
        @Query("vs_currency") vs_currency: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): CryptoChartData


}
