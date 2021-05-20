package com.example.cryptocurrencytradingsimulator.data.api

import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets")
    fun getAllCrypto(@Query("vs_currency") vs_currency: String,
                     @Query("order") order: String,
                     @Query("per_page") per_page: Int,
                     @Query("page") page: Int,
                     @Query("sparkline") sparkline: Boolean,
                     @Query("price_change_percentage") price_change_percentage: String
    ) : Call<List<Crypto>>


}
